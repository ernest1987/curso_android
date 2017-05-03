package planok.cl.android.pvi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import planok.cl.android.pvi.clases.Item;
import planok.cl.android.pvi.clases.Lugar;
import planok.cl.android.pvi.clases.Problema;
import planok.cl.android.pvi.clases.Propiedades;
import planok.cl.android.pvi.clases.Proyecto;
import planok.cl.android.pvi.clases.Recinto;

/**
 * Created by Jaimikus
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "base_datos.db";
    public static final String DBLOCATION = "/data/data/planok.cl.android.pvi/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;
    String TAG = "Base de Datos";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Abre la base de datos
     * */
    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Cierra la Base de Datos
     * */
    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    /**
     * Obtiene Todos los proyectos
     * retorna una lista de Proyectos
     * */
    public ArrayList<Proyecto> listarProyectos(){
        Proyecto proyecto = null;
        ArrayList<Proyecto> listaProyectos = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT R_PROYECTO_ID, " +
                                    "R_PROYECTO_NOMBRE, " +
                                    "R_PROYECTO_NOMBRECORTO, " +
                                    "R_PROYECTO_DIRECCION " +
                                    "FROM R_PROYECTO";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        proyecto = new Proyecto();
        listaProyectos.add(proyecto);
        if (cursor.moveToFirst()) {
            do {
                proyecto = new Proyecto(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                listaProyectos.add(proyecto);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROYECTOS --> " + cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        closeDatabase();

        // returning lables
        return listaProyectos;
    }

    public ArrayList<Propiedades> listarPropiedades(int proyecto_id){
        Propiedades propiedad = null;
        ArrayList<Propiedades> listaPropiedades = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT R_PROPIEDAD_ID, " +
                                    "R_PROYECTO_ID, " +
                                    "R_TIPO_CASA_ID, " +
                                    "R_PROPIEDAD_DIRECCION, " +
                                    "R_PROPIEDAD_ENTREGADO_POR, " +
                                    "R_PROPIEDAD_ACTA_ENTREGA " +
                                    "FROM R_PROPIEDAD WHERE R_PROYECTO_ID = " + proyecto_id;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        propiedad = new Propiedades();
        listaPropiedades.add(propiedad);
        if (cursor.moveToFirst()) {
            do {
                propiedad = new Propiedades(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                listaPropiedades.add(propiedad);
                //labels.add(cursor.getString(2));
                Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        closeDatabase();

        // returning lables
        return listaPropiedades;
    }

    public Proyecto obtenerDatosProyecto(int proyecto_id) {
        Proyecto proyecto = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT R_PROYECTO_NOMBRE, R_PROYECTO_NOMBRECORTO, R_PROYECTO_DIRECCION FROM R_PROYECTO WHERE R_PROYECTO_ID = ?", new String[]{String.valueOf(proyecto_id)});
        cursor.moveToFirst();
        proyecto = new Proyecto(proyecto_id, cursor.getString(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        closeDatabase();
        return proyecto;
    }

    public Propiedades obtenerDatosPropiedades(int propiedad_id){
        Propiedades propiedad = null;
        String selectQuery = "SELECT R_PROPIEDAD_ID, " +
                "R_PROYECTO_ID, " +
                "R_TIPO_CASA_ID, " +
                "R_PROPIEDAD_DIRECCION, " +
                "R_PROPIEDAD_ENTREGADO_POR, " +
                "R_PROPIEDAD_ACTA_ENTREGA " +
                "FROM R_PROPIEDAD WHERE R_PROPIEDAD_ID = " + propiedad_id;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        propiedad = new Propiedades(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        cursor.close();
        closeDatabase();
        return propiedad;
    }

    public List<Recinto> listarTodosRecintos(int tipo_casa_id){
        Recinto recinto = null;
        List<Recinto> listaRecinto = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_RECINTO_ID, R.R_RECINTO_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_RECINTO R ON R.R_RECINTO_ID = IP.R_RECINTO_ID WHERE R_TIPO_CASA_ID = " + tipo_casa_id +
                " ORDER BY R.R_RECINTO_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        recinto = new Recinto();
        listaRecinto.add(recinto);
        if (cursor.moveToFirst()) {
            do {
                recinto = new Recinto(cursor.getInt(0), cursor.getString(1));
                listaRecinto.add(recinto);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        closeDatabase();

        // returning lables
        return listaRecinto;
    }

    public ArrayList<String> listarNombreLugares(){
        ArrayList<String> listaLugar = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT R_LUGAR_NOMBRE FROM R_LUGAR ORDER BY R_LUGAR_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                listaLugar.add(cursor.getString(0));
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaLugar;
    }

    public List<Lugar> listarTodosLugares(int tipo_casa_id){
        Lugar lugar = null;
        List<Lugar> listaLugar = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_LUGAR_ID, L.R_LUGAR_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_LUGAR L ON L.R_LUGAR_ID = IP.R_LUGAR_ID WHERE R_TIPO_CASA_ID = " + tipo_casa_id +
                " ORDER BY L.R_LUGAR_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        lugar = new Lugar();
        listaLugar.add(lugar);
        if (cursor.moveToFirst()) {
            do {
                lugar = new Lugar(cursor.getInt(0), cursor.getString(1));
                listaLugar.add(lugar);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaLugar;
    }

    public ArrayList<String> listarNombreItems(){
        ArrayList<String> listaItems = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT R_ITEM_NOMBRE FROM R_ITEM ORDER BY R_ITEM_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                listaItems.add(cursor.getString(0));
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaItems;
    }

    public List<Item> listarTodosItems(int tipo_casa_id){
        Item item = null;
        List<Item> listaitem = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_ITEM_ID, I.R_ITEM_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_ITEM I ON I.R_ITEM_ID = IP.R_ITEM_ID WHERE R_TIPO_CASA_ID = " + tipo_casa_id +
                " ORDER BY I.R_ITEM_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        item = new Item();
        listaitem.add(item);
        if (cursor.moveToFirst()) {
            do {
                item = new Item(cursor.getInt(0), cursor.getString(1));
                listaitem.add(item);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaitem;
    }

    public List<Problema> listarTodosProblemas(int tipo_casa_id){
        Problema problema = null;
        List<Problema> listaproblema = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_PROBLEMA_ID, P.R_PROBLEMA_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_PROBLEMA P ON P.R_PROBLEMA_ID = IP.R_PROBLEMA_ID WHERE R_TIPO_CASA_ID =  " + tipo_casa_id +
                " ORDER BY P.R_PROBLEMA_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        problema = new Problema();
        listaproblema.add(problema);
        if (cursor.moveToFirst()) {
            do {
                problema = new Problema(cursor.getInt(0), cursor.getString(1));
                listaproblema.add(problema);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaproblema;
    }

    public List<Lugar> listarLugaresRecintos(int tipo_casa_id, int recintos_id){
        Lugar lugar = null;
        List<Lugar> listaLugar = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_LUGAR_ID, L.R_LUGAR_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_LUGAR L ON L.R_LUGAR_ID = IP.R_LUGAR_ID WHERE R_TIPO_CASA_ID = " + tipo_casa_id +
                " AND IP.R_RECINTO_ID = " + recintos_id + " ORDER BY L.R_LUGAR_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        lugar = new Lugar();
        listaLugar.add(lugar);
        if (cursor.moveToFirst()) {
            do {
                lugar = new Lugar(cursor.getInt(0), cursor.getString(1));
                listaLugar.add(lugar);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaLugar;
    }


    public List<Item> listarItemLugaresRecintos(int tipo_casa_id, int recintos_id, int lugar_id){
        Item item = null;
        List<Item> listaitem = new ArrayList<>();
        Log.w(TAG, "tipo_casa_id --> " + tipo_casa_id);
        Log.w(TAG, "recintos_id --> " + recintos_id);
        Log.w(TAG, "lugar_id --> " + lugar_id);
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_ITEM_ID, I.R_ITEM_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_ITEM I ON I.R_ITEM_ID = IP.R_ITEM_ID WHERE R_TIPO_CASA_ID = " + tipo_casa_id +
                " AND IP.R_RECINTO_ID = " + recintos_id + " AND IP.R_LUGAR_ID = " + lugar_id + " ORDER BY I.R_ITEM_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        item = new Item();
        listaitem.add(item);
        if (cursor.moveToFirst()) {
            do {
                item = new Item(cursor.getInt(0), cursor.getString(1));
                listaitem.add(item);
                //labels.add(cursor.getString(2));
                Log.w(TAG, "ITEM --> " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaitem;
    }

    public List<Item> listarItemsRecintos(int tipo_casa_id, int recintos_id){
        Item item = null;
        List<Item> listaitem = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_ITEM_ID, I.R_ITEM_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_ITEM I ON I.R_ITEM_ID = IP.R_ITEM_ID WHERE R_TIPO_CASA_ID = " + tipo_casa_id +
                " AND IP.R_RECINTO_ID = " + recintos_id + " ORDER BY I.R_ITEM_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        item = new Item();
        listaitem.add(item);
        if (cursor.moveToFirst()) {
            do {
                item = new Item(cursor.getInt(0), cursor.getString(1));
                listaitem.add(item);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaitem;
    }

    public List<Problema> listarProblemasRecintos(int tipo_casa_id, int recintos_id){
        Problema problema = null;
        List<Problema> listaproblema = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_PROBLEMA_ID, P.R_PROBLEMA_NOMBRE FROM R_ITEM_PROBLEMA IP \n" +
                "INNER  JOIN R_PROBLEMA P ON P.R_PROBLEMA_ID = IP.R_PROBLEMA_ID WHERE R_TIPO_CASA_ID =  " + tipo_casa_id +
                " AND IP.R_RECINTO_ID = " + recintos_id + " ORDER BY P.R_PROBLEMA_NOMBRE ASC";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        problema = new Problema();
        listaproblema.add(problema);
        if (cursor.moveToFirst()) {
            do {
                problema = new Problema(cursor.getInt(0), cursor.getString(1));
                listaproblema.add(problema);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaproblema;
    }

    public List<Problema> listarProblemas(int recinto_id, int lugar_id, int item_id){
        Problema problema = null;
        List<Problema> listaproblema = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT IP.R_PROBLEMA_ID, P.R_PROBLEMA_NOMBRE " +
                "FROM R_ITEM_PROBLEMA IP INNER JOIN R_PROBLEMA P ON P.R_PROBLEMA_ID = IP.R_PROBLEMA_ID " +
                "WHERE IP.R_RECINTO_ID = " + recinto_id +
                " AND IP.R_LUGAR_ID =" + lugar_id +
                " AND IP.R_ITEM_ID = " + item_id +
                " ORDER BY P.R_PROBLEMA_NOMBRE ASC;";
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        problema = new Problema();
        listaproblema.add(problema);
        if (cursor.moveToFirst()) {
            do {
                problema = new Problema(cursor.getInt(0), cursor.getString(1));
                listaproblema.add(problema);
                //labels.add(cursor.getString(2));
                //Log.w(TAG, "PROPIEDADES --> " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();

        // returning lables
        return listaproblema;
    }

    /*public void getListProduct() {
        int i = 0;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM R_ITEM_PROBLEMA", null);
        cursor.moveToFirst();
        Log.w(TAG,"Lista de Problemas");
        while (!cursor.isAfterLast()) {
            //Log.w(TAG, cursor.getString(1));
            cursor.moveToNext();
            i++;
        }
        Log.w(TAG, "CANTIDAD DE RESGISTROS --> " + i);
        cursor.close();
        closeDatabase();
    }*/

    /*public List<Product> getListProduct() {
        Product product = null;
        List<Product> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM PRODUCT", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
            productList.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }

    public Product getProductById(int id) {
        Product product = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM PRODUCT WHERE ID = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
        //Only 1 resul
        cursor.close();
        closeDatabase();
        return product;
    }

    public long updateProduct(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", product.getName());
        contentValues.put("PRICE", product.getPrice());
        contentValues.put("DESCRIPTION", product.getDescription());
        String[] whereArgs = {Integer.toString(product.getId())};
        openDatabase();
        long returnValue = mDatabase.update("PRODUCT", contentValues, "ID=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long addProduct(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", product.getId());
        contentValues.put("NAME", product.getName());
        contentValues.put("PRICE", product.getPrice());
        contentValues.put("DESCRIPTION", product.getDescription());
        openDatabase();
        long returnValue = mDatabase.insert("PRODUCT", null, contentValues);
        closeDatabase();
        return returnValue;
    }

    public boolean deleteProductById(int id) {
        openDatabase();
        int result = mDatabase.delete("PRODUCT", "ID =?", new String[]{String.valueOf(id)});
        closeDatabase();
        return result != 0;
    }*/
}
