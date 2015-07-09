package com.hijosdevuctir.cinemaquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;


public class MainActivity extends Activity {

    private static final long INTERVALO_CLICK = 1000;
    private DBHelper db = DBHelper.getInstance(this); // Base de datos
    private long mLastClickTime = 0; // Variable para controlar el tiempo entre pulsaciones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Button btNewGame = (Button) findViewById(R.id.new_game);
        Button btNewExtremeGame = (Button) findViewById(R.id.new_extreme_game);
        Button btResults = (Button) findViewById(R.id.results);

        Preguntas.setPreguntas(db.getAll("cine")); // Se obtienen todas las preguntas de cine de la BBDD

        btNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                lanzarJuego();
            }
        });

        btNewExtremeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                lanzarJuegoExtremo();
            }
        });

        btResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                lanzarResultados();
            }
        });
    }

    private void lanzarJuego() {
        new MaterialDialog.Builder(this)
                .title(R.string.question_num_selection)
                .content(R.string.question_num_selection_message)
                .items(R.array.game_selection)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        int num = Integer.parseInt(text.toString()); // Se obtiene el valor selecionado
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("num", num); // Se le pasa a la siguiente actividad
                        intent.putExtra("mod", 0); // Se asigna la modalidad
                        startActivity(intent); // Se inicia la actividad
                    }
                })
                .negativeText(R.string.cancel)
                .cancelable(false)
                .show();
    }

    private void lanzarJuegoExtremo() {
        new MaterialDialog.Builder(this)
                .title(R.string.extreme_game_title)
                .content(R.string.extreme_game_description)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("mod", 1); // Se asigna la modalidad
                        startActivity(intent); // Se inicia la actividad
                    }
                })
                .positiveText(R.string.continue_game)
                .negativeText(R.string.cancel)
                .cancelable(false)
                .show();
    }

    private void lanzarResultados() {
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(intent);
    }
}
