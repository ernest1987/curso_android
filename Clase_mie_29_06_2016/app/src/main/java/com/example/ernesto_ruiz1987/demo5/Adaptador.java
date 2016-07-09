package com.example.ernesto_ruiz1987.demo5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ernesto_ruiz1987 on 29-06-16.
 */
public class Adaptador extends ArrayAdapter<Tarea> {

    Context context;
    List<Tarea> ListaTareas = new ArrayList<>();
    int item_Ind;

    public Adaptador(Context context, int resource, List<Tarea> objetos) {
        super(context, resource,objetos);
        this.item_Ind=resource;
        this.context=context;
        ListaTareas=objetos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fila=inflater.inflate(R.layout.item_ind,parent,false);
        CheckBox chk= (CheckBox) fila.findViewById(R.id.CHI);
         Tarea actual = ListaTareas.get(position);
        chk.setText( actual.getNombreTarea());
        chk.setChecked(actual.getEstado()==1);
        return fila;
    }
}
