package planok.cl.android.pvi.clases;

/**
 * Created by Jaime Perez Varas on 05-05-2016.
 */
public class Propiedades {
    int propiedad_id;
    int proyecto_id;
    int tipo_casa_id;
    String propiedad_direccion;
    String propiedad_entregado_por;
    String estado_acta_entrega;

    public Propiedades() {
        this.propiedad_id = 0;
        this.proyecto_id = 0;
        this.tipo_casa_id = 0;
        this.propiedad_direccion = "";
        this.propiedad_entregado_por = "";
        this.estado_acta_entrega = "";
    }

    public Propiedades(int propiedad_id, int proyecto_id, int tipo_casa_id, String propiedad_direccion, String propiedad_entregado_por, String estado_acta_entrega) {
        this.propiedad_id = propiedad_id;
        this.proyecto_id = proyecto_id;
        this.tipo_casa_id = tipo_casa_id;
        this.propiedad_direccion = propiedad_direccion;
        this.propiedad_entregado_por = propiedad_entregado_por;
        this.estado_acta_entrega = estado_acta_entrega;
    }

    public int getPropiedad_id() {
        return propiedad_id;
    }

    public void setPropiedad_id(int propiedad_id) {
        this.propiedad_id = propiedad_id;
    }

    public int getProyecto_id() {
        return proyecto_id;
    }

    public void setProyecto_id(int proyecto_id) {
        this.proyecto_id = proyecto_id;
    }

    public int getTipo_casa_id() {
        return tipo_casa_id;
    }

    public void setTipo_casa_id(int tipo_casa_id) {
        this.tipo_casa_id = tipo_casa_id;
    }

    public String getPropiedad_direccion() {
        return propiedad_direccion;
    }

    public void setPropiedad_direccion(String propiedad_direccion) {
        this.propiedad_direccion = propiedad_direccion;
    }

    public String getPropiedad_entregado_por() {
        return propiedad_entregado_por;
    }

    public void setPropiedad_entregado_por(String propiedad_entregado_por) {
        this.propiedad_entregado_por = propiedad_entregado_por;
    }

    public String getEstado_acta_entrega() {
        return estado_acta_entrega;
    }

    public void setEstado_acta_entrega(String estado_acta_entrega) {
        this.estado_acta_entrega = estado_acta_entrega;
    }
}
