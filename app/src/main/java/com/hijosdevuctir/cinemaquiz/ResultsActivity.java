package com.hijosdevuctir.cinemaquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;


public class ResultsActivity extends Activity {

    //private LinearLayout lista;
    private Button btn;
    private BaseAdapter adaptador;
    private DBHelperResults db = DBHelperResults.getInstance(this); // Base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        adaptador = new AdaptadorResults(this);
        ListView listView = (ListView) findViewById(R.id.listPuntuaciones);
        listView.setAdapter(adaptador);

        btn = (Button) findViewById(R.id.deleteResults);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarResultados();
            }
        });

        //lista = (LinearLayout) findViewById(R.id.listaPuntuaciones);
        //actualizarLista();
    }
/*
    private void actualizarLista() {
        ArrayList<Puntuacion> listaPuntuaciones = db.getAll();
        if(listaPuntuaciones.size() > 0) {
            for(Puntuacion puntuacion : listaPuntuaciones) {
                TextView textView = new TextView(this);
                textView.setText(puntuacion.getFecha() + " " + puntuacion.getCorrectas() + " " + puntuacion.getFallidas() + " " + puntuacion.getPorcentaje());
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
*/
    private void eliminarResultados() {
        new MaterialDialog.Builder(this)
                .title(R.string.delete_results)
                .content(R.string.delete_results_message) // Mensaje con los aciertos y fallos que se llevan
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        db.clear(); // Se borran las puntuaciones
                        finish(); // Se vuelve al menú principal
                    }
                })
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .cancelable(false)
                .show();
    }

}
