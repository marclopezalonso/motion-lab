package com.example.projecte_final_android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.projecte_final_android.models.Session;
import java.util.ArrayList;
import java.util.List;
public class DBHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "motionlab.db";
    private static final int VERSION_BD = 2;
    private static final String TABLA_SESIONES = "sesiones";
    private static final String COLUMNA_ID = "id";
    private static final String COLUMNA_PASOS = "pasos";
    private static final String COLUMNA_DISTANCIA = "distancia";

    private static final String COLUMNA_DURACION = "duracion_ms";
    private static final String COLUMNA_ESTADO = "estado";
    private static final String COLUMNA_CREADA_EN = "creada_en";
    public DBHelper(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLA_SESIONES + " (" +
                        COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMNA_PASOS + " INTEGER NOT NULL, " +
                        COLUMNA_DISTANCIA + " REAL NOT NULL, " +
                        COLUMNA_DURACION + " INTEGER NOT NULL, " +
                        COLUMNA_ESTADO + " TEXT NOT NULL, " +
                        COLUMNA_CREADA_EN + " INTEGER NOT NULL" +
                        ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_SESIONES);
        onCreate(db);
    }
    public long insertarSesion(Session sesion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUMNA_PASOS, sesion.getPasos());
        valores.put(COLUMNA_DISTANCIA, sesion.getDistanciaMetros());
        valores.put(COLUMNA_DURACION, sesion.getDuracionMs());
        valores.put(COLUMNA_ESTADO, sesion.getEstado());
        valores.put(COLUMNA_CREADA_EN, sesion.getCreadaEn());
        return db.insert(TABLA_SESIONES, null, valores);
    }
    public Session obtenerSesionPorId(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLA_SESIONES,
                null,
                COLUMNA_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        try {
            if (cursor.moveToFirst()) {
                return mapearSesion(cursor);
            }
        } finally {
            cursor.close();
        }
        return null;
    }
    public Session obtenerUltimaSesion() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLA_SESIONES,
                null,
                null,
                null,
                null,
                null,
                COLUMNA_CREADA_EN + " DESC",
                "1"
        );
        try {
            if (cursor.moveToFirst()) {
                return mapearSesion(cursor);
            }
        } finally {
            cursor.close();

        }
        return null;
    }
    public List<Session> obtenerTodasLasSesiones() {
        List<Session> sesiones = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLA_SESIONES,
                null,
                null,
                null,
                null,
                null,
                COLUMNA_CREADA_EN + " DESC"
        );
        try {
            while (cursor.moveToNext()) {
                sesiones.add(mapearSesion(cursor));
            }
        } finally {
            cursor.close();
        }
        return sesiones;
    }
    private Session mapearSesion(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMNA_ID));
        int pasos = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_PASOS));
        double distancia = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMNA_DISTANCIA));
        long duracion = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMNA_DURACION));
        String estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_ESTADO));
        long creadaEn = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMNA_CREADA_EN));
        return new Session(id, pasos, distancia, duracion, estado, creadaEn);
    }
}