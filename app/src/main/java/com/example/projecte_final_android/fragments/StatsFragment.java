package com.example.projecte_final_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.projecte_final_android.R;
import java.util.Locale;

public class StatsFragment extends Fragment {
    private TextView textoTiempo;
    private TextView textoPasos;
    private TextView textoDistancia;
    private TextView textoEstado;
    private long tiempoActualMs = 0L;
    private int pasosActuales = 0;
    private double distanciaActual = 0.0;
    private String estadoActual = "Quieto";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_stats, container, false);
        textoTiempo = vista.findViewById(R.id.textoTiempo);

        textoPasos = vista.findViewById(R.id.textoPasos);
        textoDistancia = vista.findViewById(R.id.textoDistancia);
        textoEstado = vista.findViewById(R.id.textoEstado);
        pintar();
        return vista;
    }
    private void pintar() {
        if (textoTiempo != null) {
            textoTiempo.setText(getString(R.string.valor_tiempo, formatearTiempo(tiempoActualMs)));
        }
        if (textoPasos != null) {
            textoPasos.setText(getString(R.string.valor_pasos, pasosActuales));
        }
        if (textoDistancia != null) {
            textoDistancia.setText(getString(R.string.valor_distancia, distanciaActual));
        }
        if (textoEstado != null) {
            textoEstado.setText(getString(R.string.valor_estado, estadoActual));
        }
    }
    public void actualizarTiempo(long milisegundos) {
        tiempoActualMs = milisegundos;
        pintar();
    }
    public void actualizarPasos(int pasos) {
        pasosActuales = pasos;
        pintar();
    }
    public void actualizarDistancia(double distancia) {
        distanciaActual = distancia;
        pintar();
    }
    public void actualizarEstado(String estado) {
        estadoActual = estado;
        pintar();
    }
    public void reiniciar() {
        tiempoActualMs = 0L;
        pasosActuales = 0;
        distanciaActual = 0.0;
        estadoActual = "Quieto";
        pintar();
    }
    private String formatearTiempo(long milisegundos) {
        long totalSegundos = milisegundos / 1000L;
        long minutos = totalSegundos / 60L;
        long segundos = totalSegundos % 60L;
        return String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
    }
}