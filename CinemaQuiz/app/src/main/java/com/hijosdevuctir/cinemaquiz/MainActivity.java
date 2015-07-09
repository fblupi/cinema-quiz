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


public class MainActivity extends Activity {

    private static final long INTERVALO_CLICK = 1000;
    private DBHelper db = DBHelper.getInstance(this); // Base de datos
    private long mLastClickTime = 0; // Variable para controlar el tiempo entre pulsaciones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Button btNewGame = (Button) findViewById(R.id.new_game);
        Button btResults = (Button) findViewById(R.id.results);
        Button btOtherGames = (Button) findViewById(R.id.other_games);

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

        btOtherGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                lanzarOtrosJuegos();
            }
        });
    }

    private void lanzarJuego() {
        final Spinner spinner = new Spinner(this); // Se cre un spinner
        final int pad = this.getResources().getDimensionPixelSize(R.dimen.layout_padding);
        spinner.setPadding(pad,pad,pad,pad); // Se le asigna el padding por defecto de la aplicación
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.game_selection, android.R.layout.simple_list_item_1);
        spinner.setAdapter(adp); // Se le asignan al spinner los valores guardados en el string-array game_selection
        new AlertDialog.Builder(this)
                .setTitle(R.string.question_num_selection)
                .setMessage(R.string.question_num_selection_message)
                .setView(spinner)
                .setPositiveButton(R.string.continue_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // Cuando se pulsa en continuar
                        int num = Integer.parseInt(spinner.getSelectedItem().toString()); // Se obtiene el valor selecionado
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("num", num); // Se le pasa a la siguiente actividad
                        startActivity(intent); // Se inicia la actividad
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(false)
                .show();
    }

    private void lanzarResultados() {
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

    private void lanzarOtrosJuegos() {
        Intent intent = new Intent(MainActivity.this, OtherGamesActivity.class);
        startActivity(intent);
    }
}
