package com.hijosdevuctir.cinemaquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Scanner;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance = null;

    private Context context;
    private static final String DATABASE_NAME = "preguntas.db";
    private static final int DATABASE_VERSION = 4;

    public static DBHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE preguntas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tema VARCHAR(20) NOT NULL," +
                "pregunta VARCHAR(300) NOT NULL," +
                "respuesta_correcta VARCHAR(300) NOT NULL," +
                "respuesta_falsa_1 VARCHAR(300) NOT NULL," +
                "respuesta_falsa_2 VARCHAR(300) NOT NULL," +
                "respuesta_falsa_3 VARCHAR(300) NOT NULL," +
                "tipo INTEGER NOT NULL," +
                "recurso VARCHAR(300))");
        lecturaDeFicheroBD(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS preguntas");
        onCreate(db);
    }

    private void lecturaDeFicheroBD(SQLiteDatabase db) {
        String[] p;
        Scanner sc = new Scanner( this.context.getResources().openRawResource(R.raw.database) );
        ContentValues values = new ContentValues();
        while(sc.hasNextLine()) {
            p = sc.nextLine().split(";");
            values.put("tema", p[0].toString());
            values.put("pregunta",p[1].toString());
            values.put("respuesta_correcta",p[2].toString());
            values.put("respuesta_falsa_1",p[3].toString());
            values.put("respuesta_falsa_2",p[4].toString());
            values.put("respuesta_falsa_3",p[5].toString());
            values.put("tipo",Integer.parseInt(p[6].toString()));
            values.put("recurso",p[7].toString());
            db.insert("preguntas",null,values);
        }
    }

    public ArrayList<Pregunta> getAll(String tema) {
        ArrayList<Pregunta> preguntas = new ArrayList<Pregunta>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM preguntas WHERE tema = '" + tema + "'",null);
        if(c.moveToFirst()) {
            do {
                preguntas.add(new Pregunta(
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        Integer.parseInt(c.getString(7)),
                        c.getString(8)));
            } while (c.moveToNext());
        }
        db.close();

        return preguntas;
    }
}