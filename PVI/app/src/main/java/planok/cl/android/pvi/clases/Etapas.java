package planok.cl.android.pvi.clases;

/**
 * Created by Jaimikus on 16-08-16.
 */
public class Etapas {
    int etapa_id;
    int proyecto_id;
    String etapa_proyecto;

    public Etapas(int etapa_id, int proyecto_id, String etapa_proyecto) {
        this.etapa_id = etapa_id;
        this.proyecto_id = proyecto_id;
        this.etapa_proyecto = etapa_proyecto;
    }

    public Etapas() {
        this.etapa_id = 0;
        this.proyecto_id = 0;
        this.etapa_proyecto = "";
    }

    public int getEtapa_id() {
        return etapa_id;
    }

    public void setEtapa_id(int etapa_id) {
        this.etapa_id = etapa_id;
    }

    public int getProyecto_id() {
        return proyecto_id;
    }

    public void setProyecto_id(int proyecto_id) {
        this.proyecto_id = proyecto_id;
    }

    public String getEtapa_proyecto() {
        return etapa_proyecto;
    }

    public void setEtapa_proyecto(String etapa_proyecto) {
        this.etapa_proyecto = etapa_proyecto;
    }
}
