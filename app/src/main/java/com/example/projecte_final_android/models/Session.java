package com.example.projecte_final_android.models;

public class Session {
    private long id;
    private int pasos;
    private double distanciaMetros;
    private long duracionMs;
    private String estado;
    private long creadaEn;
    public Session(int pasos, double distanciaMetros, long duracionMs, String estado) {
        this(0L, pasos, distanciaMetros, duracionMs, estado, System.currentTimeMillis());
    }
    public Session(long id, int pasos, double distanciaMetros, long duracionMs, String estado, long creadaEn) {
        this.id = id;
        this.pasos = pasos;
        this.distanciaMetros = distanciaMetros;
        this.duracionMs = duracionMs;
        this.estado = estado;
        this.creadaEn = creadaEn;
    }
    public long getId() {
        return id;
    }
    public int getPasos() {
        return pasos;
    }
    public double getDistanciaMetros() {
        return distanciaMetros;
    }
    public long getDuracionMs() {
        return duracionMs;
    }

    public String getEstado() {
        return estado;
    }
    public long getCreadaEn() {
        return creadaEn;
    }
}