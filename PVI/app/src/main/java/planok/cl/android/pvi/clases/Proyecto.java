package planok.cl.android.pvi.clases;

/**
 * Created by Jaime Perez Varas on 04-05-2016.
 */
public class Proyecto {
    int proyecto_id;
    String proyecto_nombre;
    String proyecto_nombrecorto;
    String proyecto_direccion;

    public Proyecto(int proyecto_id, String proyecto_nombre, String proyecto_nombrecorto, String proyecto_direccion) {
        this.proyecto_id = proyecto_id;
        this.proyecto_nombre = proyecto_nombre;
        this.proyecto_nombrecorto = proyecto_nombrecorto;
        this.proyecto_direccion = proyecto_direccion;
    }

    public Proyecto() {
        this.proyecto_id = 0;
        this.proyecto_nombre = "";
        this.proyecto_nombrecorto = "";
        this.proyecto_direccion = "";
    }

    public int getProyecto_id() {
        return proyecto_id;
    }

    public void setProyecto_id(int proyecto_id) {
        this.proyecto_id = proyecto_id;
    }

    public String getProyecto_nombre() {
        return proyecto_nombre;
    }

    public void setProyecto_nombre(String proyecto_nombre) {
        this.proyecto_nombre = proyecto_nombre;
    }

    public String getProyecto_nombrecorto() {
        return proyecto_nombrecorto;
    }

    public void setProyecto_nombrecorto(String proyecto_nombrecorto) {
        this.proyecto_nombrecorto = proyecto_nombrecorto;
    }

    public String getProyecto_direccion() {
        return proyecto_direccion;
    }

    public void setProyecto_direccion(String proyecto_direccion) {
        this.proyecto_direccion = proyecto_direccion;
    }
}
