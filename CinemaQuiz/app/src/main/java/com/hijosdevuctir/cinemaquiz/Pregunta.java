package com.hijosdevuctir.cinemaquiz;

import java.util.Random;

public class Pregunta {

    private String pregunta;
    private int respuestaCorrecta;
    private String[] respuestas;
    private int tipo;
    private String recurso;

    public String getPregunta() {
        return pregunta;
    }

    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public int getTipo() {
        return tipo;
    }

    public String getRecurso() {
        return recurso;
    }

    public String getRespuesta(int i) {
        return respuestas[i];
    }

    public void shuffle() {
        String[] respuestasAnteriores = new String[4];
        System.arraycopy(respuestas, 0, respuestasAnteriores, 0, respuestas.length);
        int respuestaCorrectaAnterior = respuestaCorrecta;

        respuestas[0] = respuestas[1] = respuestas[2] = respuestas[3] = null;

        Random random = new Random();
        int number;

        for(int i=0; i<4; i++) {
            number = random.nextInt(4);
            while (respuestas[number] != null) {
                number = (number + 1) % 4;
            }
            respuestas[number] = respuestasAnteriores[i];
            if (respuestaCorrectaAnterior == i) respuestaCorrecta = number;
        }
    }

    public Pregunta(String pregunta, String respuestaCorrecta,
                    String respuestaIncorrecta1, String respuestaIncorrecta2,
                    String respuestaIncorrecta3, int tipo, String recurso) {
        this.pregunta = pregunta;
        this.tipo = tipo;
        this.recurso = recurso;
        respuestas = new String[4];

        this.respuestaCorrecta = 0;
        respuestas[0] = respuestaCorrecta;
        respuestas[1] = respuestaIncorrecta1;
        respuestas[2] = respuestaIncorrecta2;
        respuestas[3] = respuestaIncorrecta3;
    }

}