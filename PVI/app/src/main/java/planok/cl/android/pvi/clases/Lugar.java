package planok.cl.android.pvi.clases;

import java.util.ArrayList;

/**
 * Created by Jaime Perez Varas on 20-05-2016.
 */
public class Lugar {
    int id_lugar;
    String lugar_nombre;
    ArrayList<Item> mItemList;

    public Lugar(int id_lugar, String lugar_nombre) {
        this.id_lugar = id_lugar;
        this.lugar_nombre = lugar_nombre;
    }

    public Lugar() {
        this.id_lugar = 0;
        this.lugar_nombre = "";
        this.mItemList = null;
    }

    public Lugar(int id_lugar, String lugar_nombre, ArrayList<Item> mItemList) {
        this.id_lugar = id_lugar;
        this.lugar_nombre = lugar_nombre;
        this.mItemList = mItemList;
    }

    public int getId_lugar() {
        return id_lugar;
    }

    public void setId_lugar(int id_lugar) {
        this.id_lugar = id_lugar;
    }

    public String getLugar_nombre() {
        return lugar_nombre;
    }

    public void setLugar_nombre(String lugar_nombre) {
        this.lugar_nombre = lugar_nombre;
    }

    public ArrayList<Item> getmItemList() {
        return mItemList;
    }

    public void setmItemList(ArrayList<Item> mItemList) {
        this.mItemList = mItemList;
    }
}
