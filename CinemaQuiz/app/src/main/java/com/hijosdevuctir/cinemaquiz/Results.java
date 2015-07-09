package com.hijosdevuctir.cinemaquiz;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Results {

    private static Results instance = null;

    private static String FILE = "puntuaciones.txt";
    private Context context;

    public static Results getInstance(Context context) {
        if(instance == null) {
            instance = new Results(context);
        }
        return instance;
    }

    private Results(Context context) {
        this.context = context;
    }

    public void guardarPuntuacion(Date fecha, int aciertos, int fallos) {
        FileOutputStream f = null;
        try {
            f = context.openFileOutput(FILE, Context.MODE_APPEND);
            int porcentaje = Math.round(((float)aciertos/(aciertos+fallos))*100);
            DateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat formatoHora = new SimpleDateFormat("HH:mm");
            String formatoNumero = "%3d";
            String strDia = formatoDia.format(fecha);
            String strHora = formatoHora.format(fecha);
            String strAciertos = String.format(formatoNumero, aciertos);
            String strFallos = String.format(formatoNumero, fallos);
            String strPorcentaje = String.format(formatoNumero, porcentaje);
            String texto = strDia + " " + strHora + " " + strAciertos + " " +
                    strFallos + " " + strPorcentaje + "%\n";
            f.write(texto.getBytes());
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<String> listaPuntuaciones() {
        Vector<String> result = new Vector<String>();
        FileInputStream f = null;
        try {
            f = context.openFileInput(FILE);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
            int n = 0;
            String linea;
            do {
                linea = entrada.readLine();
                if(linea != null) {
                    result.add(linea);
                    n++;
                }
            } while (linea != null);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void borrarPuntuaciones() {
        context.deleteFile(FILE);
    }

}
