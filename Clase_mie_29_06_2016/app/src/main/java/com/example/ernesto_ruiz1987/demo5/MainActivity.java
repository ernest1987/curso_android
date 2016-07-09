package com.example.ernesto_ruiz1987.demo5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
TareaDBHelper db;
    List<Tarea> Lista;
    Adaptador adapt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db= new TareaDBHelper(this);
        Lista = db.LeerTareas();
        adapt = new Adaptador(this, R.layout.item_ind,Lista);

        ListView listView = (ListView) findViewById(R.id.lista);
        Button btn_agregar = (Button) findViewById(R.id.btn_agregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tx = (EditText) findViewById(R.id.tx);
                String s =  tx.getText().toString();
                Tarea tarea = new Tarea(s,0);
                db.AgregarTarea(tarea);
                adapt.add(tarea);
                adapt.notifyDataSetChanged();
                tx.setText("");
                tx.setHint("ingrese palabra");

            }
        });

        assert listView!=null;
        listView.setAdapter(adapt);



    }
}
