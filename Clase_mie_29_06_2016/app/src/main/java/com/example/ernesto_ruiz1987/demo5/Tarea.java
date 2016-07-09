package com.example.ernesto_ruiz1987.demo5;

/**
 * Created by ernesto_ruiz1987 on 29-06-16.
 */
public class Tarea {

    String NombreTarea;
    int Estado,Id;

    public Tarea(){
        this.NombreTarea=null;
        this.Estado=0;
    }

    public  Tarea (String nTarea, int st){
        this.NombreTarea=nTarea;
        this.Estado=st;
    }


    public String getNombreTarea() {
        return NombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        NombreTarea = nombreTarea;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
