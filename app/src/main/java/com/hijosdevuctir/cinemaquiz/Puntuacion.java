package com.hijosdevuctir.cinemaquiz;

import java.util.Date;

public class Puntuacion {

    private String fecha;
    private int correctas, fallidas, porcentaje;

    public Puntuacion(String fecha, int correctas, int fallidas, int porcentaje) {
        this.fecha = fecha;
        this.correctas = correctas;
        this.fallidas = fallidas;
        this.porcentaje = porcentaje;
    }

    public String getFecha() {
        return fecha;
    }

    public int getCorrectas() {
        return correctas;
    }

    public int getFallidas() {
        return fallidas;
    }

    public int getPorcentaje() {
        return porcentaje;
    }
}
