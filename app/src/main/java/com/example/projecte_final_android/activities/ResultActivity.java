package com.example.projecte_final_android.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.projecte_final_android.R;
import com.example.projecte_final_android.db.DBHelper;
import com.example.projecte_final_android.fragments.StatsFragment;
import com.example.projecte_final_android.models.Session;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_ID_SESION = "extra_id_sesion";
    private StatsFragment fragmentoEstadisticas;
    private DBHelper baseDatos;
    private TextView textoMetaSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textoMetaSesion = findViewById(R.id.textoMetaSesion);
        Button botonVolver = findViewById(R.id.botonVolver);
        baseDatos = new DBHelper(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentoEstadisticas = (StatsFragment) fragmentManager.findFragmentById(R.id.contenedorEstadisticas);
        if (fragmentoEstadisticas == null) {
            fragmentoEstadisticas = new StatsFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.contenedorEstadisticas, fragmentoEstadisticas)
                    .commitNow();
        }
        long idSesion = getIntent().getLongExtra(EXTRA_ID_SESION, -1L);
        Session sesion = null;
        if (idSesion > 0) {
            sesion = baseDatos.obtenerSesionPorId(idSesion);
        }
        if (sesion == null) {
            sesion = baseDatos.obtenerUltimaSesion();
        }
        if (sesion == null) {
            Toast.makeText(this, R.string.no_hay_sesiones_guardadas, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mostrarSesion(sesion);
        botonVolver.setOnClickListener(v -> finish());
    }
    private void mostrarSesion(Session sesion) {
        fragmentoEstadisticas.actualizarTiempo(sesion.getDuracionMs());
        fragmentoEstadisticas.actualizarPasos(sesion.getPasos());
        fragmentoEstadisticas.actualizarDistancia(sesion.getDistanciaMetros());
        fragmentoEstadisticas.actualizarEstado(sesion.getEstado());
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(sesion.getCreadaEn()));
        textoMetaSesion.setText(getString(R.string.meta_sesion, sesion.getId(), fecha));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (baseDatos != null) {
            baseDatos.close();
        }
    }
}