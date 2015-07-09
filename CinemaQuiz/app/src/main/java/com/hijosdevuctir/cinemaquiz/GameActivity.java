package com.hijosdevuctir.cinemaquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class GameActivity extends Activity {
    private static final long INTERVALO_CLICK = 1000;

    private int aciertos; // número de aciertos
    private int fallos; // número de fallos
    private int id; // id de la pregunta actual
    private int limite; // número de preguntas que se responderán
    private int vidas; // número de vidas
    private int modo; // modo de juego (0 normal, 1 con vidas)
    private Pregunta pregunta; // pregunta actual
    private TextView nTiempo; // TextView con el tiempo restante
    private TextView nVidas; // TextView con el número de vidas
    private Button option0; // Botón primera opción
    private Button option1; // Botón segunda opción
    private Button option2; // Botón tercera opción
    private Button option3; // Botón cuarta opción
    private CountDownTimer cronometro; // Cronómetro con la cuenta atrás
    private SoundPool soundPool; // Sonido de acierto o fallo
    private int spAciertoId; // Identificador de sonido de acierto
    private int spFalloId; // Identificador de sonido de fallo
    private long mLastClickTime = 0; // Variable para controlar el tiempo entre pulsaciones
    private Results results = Results.getInstance(this);

    // Método llamado al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        Bundle extras = getIntent().getExtras(); // Se obtienen los parámetros recibidos

        // Se busca el texto de tiempo y vidas
        nTiempo = (TextView) findViewById(R.id.nTime);
        nVidas = (TextView) findViewById(R.id.nLifes);

        vidas = 5;
        modo = extras.getInt("mod");

        if(modo == 0) {
            limite = extras.getInt("num"); // Se recoge el parámetro con el límite de preguntas
            TextView tVidas = (TextView) findViewById(R.id.lifes);
            tVidas.setVisibility(View.GONE);
            nVidas.setVisibility(View.GONE);
            if(limite > Preguntas.size() || limite < 1) { // Número incorrecto de preguntas
                limite = Preguntas.size(); // Se juegan todas las preguntas
                Toast.makeText(this, R.string.question_readjustment, Toast.LENGTH_SHORT).show();
            }
        } else {
            limite = Preguntas.size();
        }

        Preguntas.shuffle(); // Se barajan las preguntas

        aciertos = fallos = id = 0; // se inicializan las variables a 0
        pregunta = Preguntas.getPregunta(id); // se escoge la pregunta número id
        pregunta.shuffle();

        // Se buscan los botones
        option0 = (Button) findViewById(R.id.option0);
        option1 = (Button) findViewById(R.id.option1);
        option2 = (Button) findViewById(R.id.option2);
        option3 = (Button) findViewById(R.id.option3);

        // Se crean los sonidos de acierto y fallo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // Usa el nuevo constructor
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(aa)
                    .build();
            spAciertoId = soundPool.load(this, R.raw.correct, 1);
            spFalloId = soundPool.load(this, R.raw.wrong, 1);
        } else { // Usa un constructor deprecated a partir de Lollipop pero válido en versiones anteriores
            soundPool = new SoundPool(2, AudioManager.STREAM_NOTIFICATION, 1);
            spAciertoId = soundPool.load(this, R.raw.correct, 1);
            spFalloId = soundPool.load(this, R.raw.wrong, 1);
        }

        actualizarVistas(); // Se actualizan las vistas con la pregunta correspondiente

        // Listener de los botones
        option0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                cronometro.cancel();
                elegirRespuesta(0);
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                cronometro.cancel();
                elegirRespuesta(1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                cronometro.cancel();
                elegirRespuesta(2);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < INTERVALO_CLICK) { // Se ha pulsado un botón hace menos de INTERVALO_CLICK milisegundos
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime(); // Se actualiza la última pulsación
                cronometro.cancel();
                elegirRespuesta(3);
            }
        });

    }

    @Override
    // Método cuando se pulsa el botón atrás
    public void onBackPressed() {
        // String auxiliares que se mostrarán en el mensaje
        String areYouSureMessage = getString(R.string.exit_game_confirmation);
        String successMessage = getString(R.string.num_success) + aciertos;
        String errorsMessage = getString(R.string.num_errors) + fallos;

        // Se muestra un AlertDialog con los resultados finales y un botón para volver al menu principal
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_game)
                .setMessage(areYouSureMessage + "\n\n" + successMessage + "\n" + errorsMessage) // Mensaje de despedida con los aciertos y fallos que se llevan
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // Botón salir
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cronometro.cancel();
                        finalizarPartida(); // Se finaliza la partida
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setCancelable(false) // Pulsar fuera del AlertDialog no lo desactiva
                .show();
    }

    // Método llamado al pulsar un botón de respuesta
    private void elegirRespuesta(int id) {
        if(pregunta.getRespuestaCorrecta() == id) { // Acierto
            // Se colorea el botón de verde
            switch(id) {
                case 0:
                    option0.setBackgroundResource(R.drawable.btn_success);
                    option0.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                case 1:
                    option1.setBackgroundResource(R.drawable.btn_success);
                    option1.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                case 2:
                    option2.setBackgroundResource(R.drawable.btn_success);
                    option2.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                case 3:
                    option3.setBackgroundResource(R.drawable.btn_success);
                    option3.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                default:
                    break;
            }
            respuestaCorrecta();
        } else { // Fallo
            // Se colorea el botón de rojo
            switch(id) {
                case 0:
                    option0.setBackgroundResource(R.drawable.btn_error);
                    option0.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                case 1:
                    option1.setBackgroundResource(R.drawable.btn_error);
                    option1.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                case 2:
                    option2.setBackgroundResource(R.drawable.btn_error);
                    option2.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                case 3:
                    option3.setBackgroundResource(R.drawable.btn_error);
                    option3.setTextColor(getResources().getColor(R.color.buttonTextColorWhite));
                    break;
                default:
                    break;
            }
            respuestaIncorrecta();
        }
    }

    // Método para actualizar la actividad cada vez que se cambia de pregunta
    private void actualizarVistas() {
        // Vidas
        if(modo == 1) {
            nVidas.setText(Integer.toString(vidas));
        }

        // Se da el estilo de botón predeterminado a todos los botones
        option0.setBackgroundResource(R.drawable.btn_default);
        option0.setTextColor(getResources().getColor(R.color.buttonTextColor));
        option1.setBackgroundResource(R.drawable.btn_default);
        option1.setTextColor(getResources().getColor(R.color.buttonTextColor));
        option2.setBackgroundResource(R.drawable.btn_default);
        option2.setTextColor(getResources().getColor(R.color.buttonTextColor));
        option3.setBackgroundResource(R.drawable.btn_default);
        option3.setTextColor(getResources().getColor(R.color.buttonTextColor));

        // Se cargan la imagen y el RelativeLayout donde se encuentran los controles de la música
        ImageView image = (ImageView) findViewById(R.id.image);

        if (this.pregunta.getTipo() == 1) { // Si es modo pregunta con imagen se carga el recurso
            image.setImageResource(getImageId(this, this.pregunta.getRecurso()));
        } else { // en caso contrario se carga la imagen genérica
            image.setImageResource(R.drawable.generic_question);
        }

        // Se carga la pregunta y se le da el texto correspondiente
        TextView pregunta = (TextView) findViewById(R.id.question);
        pregunta.setText(this.pregunta.getPregunta());

        // Se carga el texto en todos los botones
        option0.setText(this.pregunta.getRespuesta(0));
        option1.setText(this.pregunta.getRespuesta(1));
        option2.setText(this.pregunta.getRespuesta(2));
        option3.setText(this.pregunta.getRespuesta(3));

        // Se genera la cuenta atrás
        cuentaAtras();
    }

    // Método que se activa cuando la respuesta pulsada es correcta
    private void respuestaCorrecta() {
        // Se actualizan las variables correspondientes
        aciertos++;
        id++;

        // String auxiliares que se mostrarán en el mensaje
        String successMessage = getString(R.string.num_success) + aciertos;
        String errorsMessage = getString(R.string.num_errors) + fallos;

        // Sonido de acierto
        soundPool.play(spAciertoId, 0.25f, 0.25f, 1, 0, 1);

        // Se muestra un AlertDialog con los resultados parciales y un botón para continuar
        new AlertDialog.Builder(this)
                .setTitle(R.string.success_message)
                .setMessage(successMessage + "\n" + errorsMessage) // Mensaje con los aciertos y fallos que se llevan
                .setPositiveButton(R.string.continue_game, new DialogInterface.OnClickListener() { // Botón continuar
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (id == limite) { // Se ha respondido todas las preguntas
                            juegoCompletado();
                        } else { // Se pasa a la siguiente pregunta
                            siguientePregunta();
                        }
                    }
                })
                .setCancelable(false) // Pulsar fuera del AlertDialog no lo desactiva
                .show();
    }

    // Método que se activa cuando la respuesta pulsada es correcta
    private void respuestaIncorrecta() {
        // Se actualizan las variables correspondientes
        fallos++;
        id++;
        if (modo == 1) {
            vidas--;
        }

        // String auxiliares que se mostrarán en el mensaje
        String successMessage = getString(R.string.num_success) + aciertos;
        String errorsMessage = getString(R.string.num_errors) + fallos;

        // Sonido de fallo
        soundPool.play(spFalloId, 0.25f, 0.25f, 1, 0, 1);

        // Se muestra un AlertDialog con los resultados parciales y un botón para continuar
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_message)
                .setMessage(successMessage + "\n" + errorsMessage) // Mensaje con los aciertos y fallos que se llevan
                .setNegativeButton(R.string.exit_game, new DialogInterface.OnClickListener() { // Botón salir
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalizarPartida(); // finaliza la partida
                    }
                })
                .setPositiveButton(R.string.continue_game, new DialogInterface.OnClickListener() { // Botón continuar
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (id == limite || (modo == 1 && vidas < 0)) { // Se ha respondido todas las preguntas
                            juegoCompletado();
                        } else { // Se pasa a la siguiente pregunta
                            siguientePregunta();
                        }
                    }
                })
                .setCancelable(false) // Pulsar fuera del AlertDialog no lo desactiva
                .show();
    }

    // Método que se activa cuando se responden todas las preguntas
    private void juegoCompletado() {
        // String auxiliares que se mostrarán en el mensaje
        String finishMessage = getString(R.string.finish_message);
        String successMessage = getString(R.string.num_success) + aciertos;
        String errorsMessage = getString(R.string.num_errors) + fallos;

        // Se muestra un AlertDialog con los resultados finales y un botón para volver al menu principal
        new AlertDialog.Builder(this)
                .setTitle(R.string.finish_title)
                .setMessage(finishMessage + "\n\n" + successMessage + "\n" + errorsMessage) // Mensaje de despedida con los aciertos y fallos que se llevan
                .setPositiveButton(R.string.exit_game, new DialogInterface.OnClickListener() { // Botón salir
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalizarPartida(); // Se finaliza la partida
                    }
                })
                .setCancelable(false) // Pulsar fuera del AlertDialog no lo desactiva
                .show();
    }

    // Método para cambiar de pregunta y actualizar los campos de la actividad
    private void siguientePregunta() {
        pregunta = Preguntas.getPregunta(id);
        pregunta.shuffle();
        actualizarVistas();
    }

    // Método que se activa cuando se finaliza la partida
    private void finalizarPartida() {
        results.guardarPuntuacion(new Date(), aciertos, fallos);
        finish();
    }

    // Método para obtener el id de un recurso en forma de imagen a partir de un String
    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    private void cuentaAtras() {
        cronometro = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                nTiempo.setText(Long.toString(millisUntilFinished/1000));
            }
            @Override
            public void onFinish() {
                respuestaIncorrecta();
            }
        }.start();
    }
}
