package com.example.projecte_final_android.sensor;

import com.example.projecte_final_android.models.MotionSnapshot;
import java.util.ArrayDeque;
import java.util.Deque;
public class MotionAnalyzer {
    public static final String ESTADO_QUIETO = "Quieto";
    public static final String ESTADO_CAMINANDO = "Caminando";
    public static final String ESTADO_CORRIENDO = "Corriendo";
    private static final float ALFA_FILTRO = 0.8f;
    private static final float UMBRAL_PASO = 1.2f;
    private static final long TIEMPO_MINIMO_ENTRE_PASOS = 280L;
    private static final int TAMANO_VENTANA = 30;
    private static final long VENTANA_CADENCIA_MS = 10_000L;
    private final float[] gravedad = new float[3];
    private final Deque<Float> ventanaMagnitudes = new ArrayDeque<>();
    private final Deque<Long> tiemposPasos = new ArrayDeque<>();

    private float magnitudAnterior = 0f;
    private float magnitudAnterior2 = 0f;
    private int pasos = 0;
    private double distanciaMetros = 0.0;
    private String estadoActual = ESTADO_QUIETO;
    private float longitudPasoMetros = 0.75f;
    private long ultimoPasoMs = 0L;
    public synchronized void reiniciar() {
        gravedad[0] = 0f;
        gravedad[1] = 0f;
        gravedad[2] = 0f;
        ventanaMagnitudes.clear();
        tiemposPasos.clear();
        magnitudAnterior = 0f;
        magnitudAnterior2 = 0f;
        pasos = 0;
        distanciaMetros = 0.0;
        estadoActual = ESTADO_QUIETO;
        ultimoPasoMs = 0L;
    }
    public synchronized void establecerLongitudPasoMetros(float longitudPasoMetros) {
        if (longitudPasoMetros > 0f) {
            this.longitudPasoMetros = longitudPasoMetros;
        }
    }
    public synchronized MotionSnapshot procesarMuestra(float x, float y, float z, long instanteMs) {
        gravedad[0] = ALFA_FILTRO * gravedad[0] + (1 - ALFA_FILTRO) * x;
        gravedad[1] = ALFA_FILTRO * gravedad[1] + (1 - ALFA_FILTRO) * y;
        gravedad[2] = ALFA_FILTRO * gravedad[2] + (1 - ALFA_FILTRO) * z;
        float linealX = x - gravedad[0];
        float linealY = y - gravedad[1];
        float linealZ = z - gravedad[2];
        float magnitudFiltrada = (float) Math.sqrt(
                linealX * linealX +
                        linealY * linealY +
                        linealZ * linealZ
        );
        agregarMagnitud(magnitudFiltrada);
        detectarPaso(magnitudFiltrada, instanteMs);
        distanciaMetros = pasos * longitudPasoMetros;
        estadoActual = clasificarEstado(instanteMs);
        magnitudAnterior2 = magnitudAnterior;
        magnitudAnterior = magnitudFiltrada;
        return new MotionSnapshot(
                pasos,
                distanciaMetros,
                estadoActual,
                magnitudFiltrada,
                calcularMediaVentana(),
                calcularDesviacionVentana(),
                calcularCadenciaPasos(instanteMs)
        );
    }
    private void agregarMagnitud(float magnitud) {
        ventanaMagnitudes.addLast(magnitud);
        while (ventanaMagnitudes.size() > TAMANO_VENTANA) {
            ventanaMagnitudes.removeFirst();
        }
    }
    private void detectarPaso(float magnitudActual, long instanteMs) {
        boolean hayPico = magnitudAnterior > magnitudAnterior2
                && magnitudAnterior >= magnitudActual
                && magnitudAnterior > UMBRAL_PASO;
        boolean respetaTiempo = (instanteMs - ultimoPasoMs) >= TIEMPO_MINIMO_ENTRE_PASOS;
        if (hayPico && respetaTiempo) {

            pasos++;
            ultimoPasoMs = instanteMs;
            tiemposPasos.addLast(instanteMs);
            limpiarPasosAntiguos(instanteMs);
        }
    }
    private void limpiarPasosAntiguos(long instanteMs) {
        while (!tiemposPasos.isEmpty() && (instanteMs - tiemposPasos.peekFirst()) > VENTANA_CADENCIA_MS) {
            tiemposPasos.removeFirst();
        }
    }
    private String clasificarEstado(long instanteMs) {
        float media = calcularMediaVentana();
        float desviacion = calcularDesviacionVentana();
        float cadencia = calcularCadenciaPasos(instanteMs);
        if (ventanaMagnitudes.size() < 8 && pasos == 0) {
            return ESTADO_QUIETO;
        }
        if (media < 0.8f && desviacion < 0.35f && cadencia < 20f) {
            return ESTADO_QUIETO;
        }
        if (cadencia >= 125f || media >= 2.0f || desviacion >= 1.3f) {
            return ESTADO_CORRIENDO;
        }
        return ESTADO_CAMINANDO;
    }
    private float calcularMediaVentana() {
        if (ventanaMagnitudes.isEmpty()) {
            return 0f;
        }
        float suma = 0f;
        for (float valor : ventanaMagnitudes) {
            suma += valor;
        }
        return suma / ventanaMagnitudes.size();
    }
    private float calcularDesviacionVentana() {
        if (ventanaMagnitudes.isEmpty()) {
            return 0f;
        }
        float media = calcularMediaVentana();
        float suma = 0f;
        for (float valor : ventanaMagnitudes) {
            float diferencia = valor - media;
            suma += diferencia * diferencia;
        }
        return (float) Math.sqrt(suma / ventanaMagnitudes.size());
    }
    private float calcularCadenciaPasos(long instanteMs) {
        limpiarPasosAntiguos(instanteMs);
        if (tiemposPasos.size() < 2) {
            return 0f;
        }
        long primero = tiemposPasos.peekFirst();
        long ultimo = tiemposPasos.peekLast();
        long transcurrido = Math.max(1L, ultimo - primero);
        return (tiemposPasos.size() - 1) * 60_000f / transcurrido;
    }
    public synchronized int obtenerPasos() {
        return pasos;
    }
    public synchronized double obtenerDistanciaMetros() {
        return distanciaMetros;
    }
    public synchronized String obtenerEstadoActual() {

        return estadoActual;
    }
    public synchronized float obtenerLongitudPasoMetros() {
        return longitudPasoMetros;
    }
}