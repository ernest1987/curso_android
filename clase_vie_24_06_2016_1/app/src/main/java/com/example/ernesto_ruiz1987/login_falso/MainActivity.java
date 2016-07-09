package com.example.ernesto_ruiz1987.login_falso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button ingresar;
    EditText nombre;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ingresar= (Button) findViewById(R.id.ingresar);
        nombre= (EditText)findViewById(R.id.nombre);
        password=(EditText)findViewById(R.id.password);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidarLogin()){
                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean ValidarLogin(){
     double  n= Math.random();
        if ((int)n%2==0){
            Toast.makeText(getApplicationContext(),String.valueOf(n),Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(getApplicationContext(),String.valueOf(n),Toast.LENGTH_SHORT).show();
            return false;
        }

    }




}
