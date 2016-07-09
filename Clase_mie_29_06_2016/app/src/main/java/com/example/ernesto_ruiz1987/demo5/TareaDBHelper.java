package com.example.ernesto_ruiz1987.demo5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ernesto_ruiz1987 on 29-06-16.
 */
public class TareaDBHelper extends SQLiteOpenHelper {

    private  static final int DATABASE_V=1;
    private  static final String DATABASE_NOMBRE="LISTATAREAS";// base de datos
    private  static final String TABLA_TAREA ="TAREAS";// tabla base de datos


    //Campos Tabla



    String KEY_ID="id";
    String KEY_NOMBRETAREA="nombretarea";
    String KEY_ESTADO="estado";


    public TareaDBHelper(Context context){

        super(context,DATABASE_NOMBRE,null,DATABASE_V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String  sql="CREATE TABLE IF NOT EXISTS "+ TABLA_TAREA +" ( " +
        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        KEY_NOMBRETAREA + " TEXT, " +
        KEY_ESTADO + " INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLA_TAREA);
        onCreate(db);

    }


    public void AgregarTarea(Tarea tarea){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues datos = new ContentValues();
        datos.put(KEY_NOMBRETAREA,tarea.getNombreTarea());
        datos.put(KEY_ESTADO,tarea.getEstado());
        db.insert(TABLA_TAREA,null,datos);

        db.close();
    }


    public List<Tarea> LeerTareas(){
        List<Tarea> ListaTareas= new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLA_TAREA;
        SQLiteDatabase db =this.getWritableDatabase();

        Cursor cursor= db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do{
                Tarea tarea = new Tarea();
                    tarea.setId(cursor.getInt (0));
                    tarea.setNombreTarea (cursor.getString(1));
                    tarea.setEstado(cursor.getInt(2));
                    ListaTareas.add(tarea);
            }while (cursor.moveToNext());



        }

        return ListaTareas;
    }

}
