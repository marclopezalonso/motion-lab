package com.example.projecte_final_android.models;

public class MotionSnapshot {
    private final int pasos;
    private final double distanciaMetros;
    private final String estado;
    private final float magnitudFiltrada;
    private final float mediaVentana;
    private final float desviacionVentana;
    private final float cadenciaPasos;
    public MotionSnapshot(int pasos,
                          double distanciaMetros,
                          String estado,
                          float magnitudFiltrada,
                          float mediaVentana,
                          float desviacionVentana,
                          float cadenciaPasos) {
        this.pasos = pasos;
        this.distanciaMetros = distanciaMetros;
        this.estado = estado;
        this.magnitudFiltrada = magnitudFiltrada;
        this.mediaVentana = mediaVentana;
        this.desviacionVentana = desviacionVentana;
        this.cadenciaPasos = cadenciaPasos;
    }
    public int getPasos() {
        return pasos;
    }
    public double getDistanciaMetros() {
        return distanciaMetros;
    }
    public String getEstado() {
        return estado;
    }
    public float getMagnitudFiltrada() {
        return magnitudFiltrada;
    }
    public float getMediaVentana() {
        return mediaVentana;
    }
    public float getDesviacionVentana() {
        return desviacionVentana;
    }
    public float getCadenciaPasos() {
        return cadenciaPasos;
    }
}