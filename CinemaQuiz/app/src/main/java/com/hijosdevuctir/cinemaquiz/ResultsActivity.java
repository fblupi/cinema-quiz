package com.hijosdevuctir.cinemaquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Vector;


public class ResultsActivity extends Activity {

    private Results puntuaciones = Results.getInstance(this);
    private LinearLayout lista;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        btn = (Button) findViewById(R.id.deleteResults);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarResultados();
            }
        });

        lista = (LinearLayout) findViewById(R.id.listaPuntuaciones);
        actualizarLista();
    }

    private void actualizarLista() {
        Vector<String> listaPuntuaciones = puntuaciones.listaPuntuaciones();
        if(listaPuntuaciones.size() > 0) {
            for (String puntuacion : listaPuntuaciones) {
                TextView textView = new TextView(this);
                textView.setText(puntuacion);
                textView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                lista.addView(textView);
            }
        } else {
            TextView textView = new TextView(this);
            textView.setText(R.string.no_results);
            textView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            lista.addView(textView);
        }
    }

    private void eliminarResultados() {
        new MaterialDialog.Builder(this)
                .title(R.string.delete_results)
                .content(R.string.delete_results_message) // Mensaje con los aciertos y fallos que se llevan
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        puntuaciones.borrarPuntuaciones(); // Se borran las puntuaciones
                        finish(); // Se vuelve al menú principal
                    }
                })
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .cancelable(false)
                .show();
    }

}
