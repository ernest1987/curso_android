package planok.cl.android.pvi.clases;

import java.util.ArrayList;

/**
 * Created by Jaime Perez Varas on 20-05-2016.
 */
public class Recinto {
    int id_recinto;
    String recinto_nombre;
    ArrayList<Lugar> mLugarList;

    public Recinto(int id_recinto, String recinto_nombre) {
        this.id_recinto = id_recinto;
        this.recinto_nombre = recinto_nombre;
    }

    public Recinto() {
        this.id_recinto = 0;
        this.recinto_nombre = "";
        this.mLugarList = null;
    }

    public Recinto(int id_recinto, String recinto_nombre, ArrayList<Lugar> mLugarList) {
        this.id_recinto = id_recinto;
        this.recinto_nombre = recinto_nombre;
        this.mLugarList = mLugarList;
    }

    public int getId_recinto() {
        return id_recinto;
    }

    public void setId_recinto(int id_recinto) {
        this.id_recinto = id_recinto;
    }

    public String getRecinto_nombre() {
        return recinto_nombre;
    }

    public void setRecinto_nombre(String recinto_nombre) {
        this.recinto_nombre = recinto_nombre;
    }

    public ArrayList<Lugar> getmLugarList() {
        return mLugarList;
    }

    public void setmLugarList(ArrayList<Lugar> mLugarList) {
        this.mLugarList = mLugarList;
    }
}
