package com.hijosdevuctir.cinemaquiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Preguntas {
    private static List<Pregunta> vectorPreguntas;

    public Preguntas() { }

    public static void setPreguntas(ArrayList<Pregunta> preguntas) {
        vectorPreguntas = preguntas;
    }

    public static void shuffle() {
        Collections.shuffle(vectorPreguntas);
    }

    public static Pregunta getPregunta(int id) {
        return vectorPreguntas.get(id);
    }

    public static int size() {
        return vectorPreguntas.size();
    }

}