package planok.cl.android.pvi.clases;

import java.util.ArrayList;

/**
 * Created by Jaime Perez Varas on 20-05-2016.
 */
public class Item {
    int id_item;
    String item_nombre;

    public Item(int id_item, String item_nombre) {
        this.id_item = id_item;
        this.item_nombre = item_nombre;
    }

    public Item() {
        this.id_item = 0;
        this.item_nombre = "";
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public String getItem_nombre() {
        return item_nombre;
    }

    public void setItem_nombre(String item_nombre) {
        this.item_nombre = item_nombre;
    }
    
}
