package com.example.projecte_final_android.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.projecte_final_android.R;
import com.example.projecte_final_android.db.DBHelper;
import com.example.projecte_final_android.fragments.StatsFragment;
import com.example.projecte_final_android.models.MotionSnapshot;
import com.example.projecte_final_android.models.Session;
import com.example.projecte_final_android.sensor.MotionAnalyzer;
import com.example.projecte_final_android.views.GraphView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MotionActivity extends AppCompatActivity implements SensorEventListener {
    private enum EstadoSesion {
        DETENIDA,
        EN_MARCHA,
        PAUSADA
    }
    private SensorManager administradorSensores;
    private Sensor acelerometro;
    private HandlerThread hiloSensor;
    private Handler manejadorSensor;
    private Handler manejadorUi = new Handler(Looper.getMainLooper());
    private MotionAnalyzer analizadorMovimiento;
    private GraphView vistaGrafica;
    private StatsFragment fragmentoEstadisticas;
    private Button botonIniciar;
    private Button botonPausar;
    private Button botonFinalizar;
    private EstadoSesion estadoSesion = EstadoSesion.DETENIDA;
    private boolean sensorRegistrado = false;
    private long instanteInicioActivo = 0L;
    private long tiempoAcumuladoMs = 0L;
    private final Handler manejadorTiempo = new Handler(Looper.getMainLooper());
    private final ExecutorService hiloBaseDatos = Executors.newSingleThreadExecutor();
    private DBHelper baseDatos;
    private final Runnable actualizadorTiempo = new Runnable() {
        @Override
        public void run() {
            if (estadoSesion == EstadoSesion.EN_MARCHA) {
                fragmentoEstadisticas.actualizarTiempo(obtenerTiempoMostradoMs());
                manejadorTiempo.postDelayed(this, 500);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        vistaGrafica = findViewById(R.id.vistaGrafica);
        botonIniciar = findViewById(R.id.botonIniciar);
        botonPausar = findViewById(R.id.botonPausar);
        botonFinalizar = findViewById(R.id.botonFinalizar);
        baseDatos = new DBHelper(this);
        analizadorMovimiento = new MotionAnalyzer();
        hiloSensor = new HandlerThread("HiloSensorMotionLab");
        hiloSensor.start();
        manejadorSensor = new Handler(hiloSensor.getLooper());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentoEstadisticas = (StatsFragment) fragmentManager.findFragmentById(R.id.contenedorEstadisticas);
        if (fragmentoEstadisticas == null) {
            fragmentoEstadisticas = new StatsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedorEstadisticas, fragmentoEstadisticas)
                    .commitNow();
        }

        administradorSensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (administradorSensores != null) {
            acelerometro = administradorSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (acelerometro == null) {
            Toast.makeText(this, R.string.sin_acelerometro, Toast.LENGTH_LONG).show();
            botonIniciar.setEnabled(false);
            botonPausar.setEnabled(false);
            botonFinalizar.setEnabled(false);
        }
        fragmentoEstadisticas.reiniciar();
        vistaGrafica.reiniciar();
        actualizarEstadoBotones();
        botonIniciar.setOnClickListener(v -> iniciarOReanudarSesion());
        botonPausar.setOnClickListener(v -> pausarSesion());
        botonFinalizar.setOnClickListener(v -> finalizarSesion());
    }
    private void iniciarOReanudarSesion() {
        if (acelerometro == null || estadoSesion == EstadoSesion.EN_MARCHA) {
            return;
        }
        if (estadoSesion == EstadoSesion.DETENIDA) {
            analizadorMovimiento.reiniciar();
            vistaGrafica.reiniciar();
            fragmentoEstadisticas.reiniciar();
            tiempoAcumuladoMs = 0L;
        }
        instanteInicioActivo = SystemClock.elapsedRealtime();
        estadoSesion = EstadoSesion.EN_MARCHA;
        registrarSensorSiHaceFalta();
        manejadorTiempo.removeCallbacks(actualizadorTiempo);
        manejadorTiempo.post(actualizadorTiempo);
        actualizarEstadoBotones();
        Toast.makeText(this, R.string.sesion_iniciada, Toast.LENGTH_SHORT).show();
    }
    private void pausarSesion() {
        if (estadoSesion != EstadoSesion.EN_MARCHA) {
            return;
        }
        tiempoAcumuladoMs += SystemClock.elapsedRealtime() - instanteInicioActivo;
        estadoSesion = EstadoSesion.PAUSADA;
        desregistrarSensorSiHaceFalta();
        manejadorTiempo.removeCallbacks(actualizadorTiempo);
        actualizarEstadoBotones();
        Toast.makeText(this, R.string.sesion_pausada, Toast.LENGTH_SHORT).show();
    }
    private void finalizarSesion() {
        if (estadoSesion == EstadoSesion.EN_MARCHA) {
            pausarSesion();
        }
        long duracionTotalMs = tiempoAcumuladoMs;
        int pasos = analizadorMovimiento.obtenerPasos();
        double distanciaMetros = analizadorMovimiento.obtenerDistanciaMetros();
        String estado = analizadorMovimiento.obtenerEstadoActual();
        if (duracionTotalMs <= 0L && pasos == 0) {
            Toast.makeText(this, R.string.no_hay_datos, Toast.LENGTH_SHORT).show();
            return;
        }
        Session sesion = new Session(
                pasos,
                distanciaMetros,
                duracionTotalMs,
                estado
        );
        hiloBaseDatos.execute(() -> {

            long idSesion = baseDatos.insertarSesion(sesion);
            manejadorUi.post(() -> {
                Toast.makeText(MotionActivity.this,
                        getString(R.string.sesion_guardada_con_id, idSesion),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MotionActivity.this, ResultActivity.class);
                intent.putExtra(ResultActivity.EXTRA_ID_SESION, idSesion);
                startActivity(intent);
                finish();
            });
        });
    }
    private void registrarSensorSiHaceFalta() {
        if (!sensorRegistrado && administradorSensores != null && acelerometro != null) {
            administradorSensores.registerListener(
                    this,
                    acelerometro,
                    SensorManager.SENSOR_DELAY_GAME,
                    manejadorSensor
            );
            sensorRegistrado = true;
        }
    }
    private void desregistrarSensorSiHaceFalta() {
        if (sensorRegistrado && administradorSensores != null) {
            administradorSensores.unregisterListener(this);
            sensorRegistrado = false;
        }
    }
    private long obtenerTiempoMostradoMs() {
        if (estadoSesion == EstadoSesion.EN_MARCHA) {
            return tiempoAcumuladoMs + (SystemClock.elapsedRealtime() - instanteInicioActivo);
        }
        return tiempoAcumuladoMs;
    }
    private void actualizarEstadoBotones() {
        botonIniciar.setText(estadoSesion == EstadoSesion.PAUSADA
                ? R.string.reanudar
                : R.string.iniciar);
        botonPausar.setEnabled(estadoSesion == EstadoSesion.EN_MARCHA);
        botonFinalizar.setEnabled(estadoSesion == EstadoSesion.EN_MARCHA || estadoSesion == EstadoSesion.PAUSADA);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (estadoSesion == EstadoSesion.EN_MARCHA) {
            registrarSensorSiHaceFalta();
            manejadorTiempo.removeCallbacks(actualizadorTiempo);
            manejadorTiempo.post(actualizadorTiempo);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (estadoSesion == EstadoSesion.EN_MARCHA) {
            pausarSesion();
        } else {
            desregistrarSensorSiHaceFalta();
            manejadorTiempo.removeCallbacks(actualizadorTiempo);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        desregistrarSensorSiHaceFalta();
        manejadorTiempo.removeCallbacksAndMessages(null);
        manejadorUi.removeCallbacksAndMessages(null);
        if (hiloSensor != null) {
            hiloSensor.quitSafely();
        }

        hiloBaseDatos.shutdownNow();
        if (baseDatos != null) {
            baseDatos.close();
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (estadoSesion != EstadoSesion.EN_MARCHA) {
            return;
        }
        float ejeX = event.values[0];
        float ejeY = event.values[1];
        float ejeZ = event.values[2];
        long instanteMs = SystemClock.elapsedRealtime();

        MotionSnapshot resumen = analizadorMovimiento.procesarMuestra(ejeX, ejeY, ejeZ, instanteMs);
        manejadorUi.post(() -> {
            if (estadoSesion != EstadoSesion.EN_MARCHA) {
                return;
            }
            vistaGrafica.agregarPunto(resumen.getMagnitudFiltrada());
            fragmentoEstadisticas.actualizarTiempo(obtenerTiempoMostradoMs());
            fragmentoEstadisticas.actualizarPasos(resumen.getPasos());
            fragmentoEstadisticas.actualizarDistancia(resumen.getDistanciaMetros());
            fragmentoEstadisticas.actualizarEstado(resumen.getEstado());
        });
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}