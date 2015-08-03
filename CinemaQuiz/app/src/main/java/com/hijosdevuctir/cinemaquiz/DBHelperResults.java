package com.hijosdevuctir.cinemaquiz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelperResults extends SQLiteOpenHelper {

    private static DBHelperResults instance = null;

    private Context context;
    private static final String DATABASE_NAME = "puntuaciones.db";
    private static final int DATABASE_VERSION = 1;

    public static DBHelperResults getInstance(Context context) {
        if(instance == null) {
            instance = new DBHelperResults(context);
        }
        return instance;
    }

    private DBHelperResults(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE puntuaciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha DATE NOT NULL," +
                "correctas INTEGER NOT NULL," +
                "fallidas INTEGER NOT NULL," +
                "porcentaje INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS puntuaciones");
        onCreate(db);
    }

    public void add(Date fecha, int correctas, int fallidas) {
        int porcentaje = Math.round(((float) correctas / (correctas + fallidas)) * 100);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fecha", dateFormat.format(fecha));
        values.put("correctas", Integer.toString(correctas));
        values.put("fallidas", Integer.toString(fallidas));
        values.put("porcentaje", Integer.toString(porcentaje));
        db.insert("puntuaciones", null, values);
        db.close();
    }

    public int getCount() {
        int count;

        SQLiteDatabase db = this.getReadableDatabase();
        count = db.rawQuery("SELECT id FROM puntuaciones", null).getCount();
        db.close();

        return count;
    }

    public Puntuacion get(int i) {
        Puntuacion resultado;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM puntuaciones", null);
        c.moveToFirst();
        for(int n = 0; n < i; n++) {
            c.moveToNext();
        }
        resultado = new Puntuacion(
                c.getString(1),
                Integer.parseInt(c.getString(2)),
                Integer.parseInt(c.getString(3)),
                Integer.parseInt(c.getString(4)));
        db.close();

        return resultado;
    }

    public ArrayList<Puntuacion> getAll() {
        ArrayList<Puntuacion> puntuaciones = new ArrayList<Puntuacion>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM puntuaciones", null);
        if(c.moveToFirst()) {
            do {
                puntuaciones.add(new Puntuacion(
                        c.getString(1),
                        Integer.parseInt(c.getString(2)),
                        Integer.parseInt(c.getString(3)),
                        Integer.parseInt(c.getString(4))));
            } while (c.moveToNext());
        }
        db.close();

        return puntuaciones;
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM puntuaciones");
        db.close();
    }
}
