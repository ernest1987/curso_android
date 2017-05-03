package planok.cl.android.pvi.clases;

/**
 * Created by Jaime Perez Varas on 20-05-2016.
 */
public class Problema {
    int id_problema;
    String problema_nombre;

    public Problema(int id_problema, String problema_nombre) {
        this.id_problema = id_problema;
        this.problema_nombre = problema_nombre;
    }

    public Problema() {
        this.id_problema = 0;
        this.problema_nombre = "";
    }

    public int getId_problema() {
        return id_problema;
    }

    public void setId_problema(int id_problema) {
        this.id_problema = id_problema;
    }

    public String getProblema_nombre() {
        return problema_nombre;
    }

    public void setProblema_nombre(String problema_nombre) {
        this.problema_nombre = problema_nombre;
    }
}
