package com.hijosdevuctir.cinemaquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AdaptadorResults extends BaseAdapter {

    private DBHelperResults db; // Base de datos
    private LayoutInflater inflador;
    TextView nAciertos, nFallos, fecha;
    RatingBar porcentaje;

    public AdaptadorResults(Context context) {
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = DBHelperResults.getInstance(context);
    }

    public View getView(int i, View vistaReciclada, ViewGroup padre) {
        Puntuacion puntuacion = db.get(i);
        if(vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.result, null);
        }
        fecha = (TextView) vistaReciclada.findViewById(R.id.listFecha);
        nAciertos = (TextView) vistaReciclada.findViewById(R.id.listNAciertos);
        nFallos = (TextView) vistaReciclada.findViewById(R.id.listNFallos);
        porcentaje = (RatingBar) vistaReciclada.findViewById(R.id.listPorcentaje);

        fecha.setText(puntuacion.getFecha());
        nAciertos.setText(Integer.toString(puntuacion.getCorrectas()));
        nFallos.setText(Integer.toString(puntuacion.getFallidas()));
        porcentaje.setRating(puntuacion.getPorcentaje()/10);

        return vistaReciclada;
    }

    public int getCount() {
        return db.getCount();
    }

    public Object getItem(int i) {
        return db.get(i);
    }

    public long getItemId(int i) {
        return i;
    }
}
