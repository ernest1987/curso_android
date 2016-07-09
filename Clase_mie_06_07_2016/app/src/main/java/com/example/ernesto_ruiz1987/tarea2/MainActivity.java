package com.example.ernesto_ruiz1987.tarea2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText Enombre;
    TextView Nombre;
    Button Ingresar;
    SharedPreferences Usuario;
    SharedPreferences.Editor EUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Enombre = (EditText) findViewById(R.id.ENombre);
        Nombre = (TextView) findViewById(R.id.Nombre);
        Ingresar = (Button) findViewById(R.id.Ingresar);

        Usuario = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        EUsuario=Usuario.edit();

        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Enombre.getText().toString().trim().length()>0){
                    EUsuario.putString("NOMBRE",Enombre.getText().toString());
                    EUsuario.apply();
                }


            }
        });

        if(!Usuario.getString("NOMBRE","null").equals("null")){
            Nombre.setText(Usuario.getString("NOMBRE","null"));
            Enombre.setVisibility(View.GONE);
            Ingresar.setVisibility(View.GONE);
            //EUsuario.clear();
            //EUsuario.apply();
            TimerTask ts = new TimerTask() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(i);
                }
            };


            Timer time = new Timer();
            time.schedule(ts,2000);

        }
    }
}
