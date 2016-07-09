package com.example.ernesto_ruiz1987.tarea2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {
 Button Tomar_Foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Tomar_Foto= (Button) findViewById(R.id.Tomar_Foto);
        Tomar_Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long nombre= System.currentTimeMillis();
                String path = Environment.getExternalStorageDirectory() +"DCIM/Camera/"+nombre+".jpg";
                File foto =new File (path);
                Intent intent= new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, Uri.fromFile(foto));
                startActivityForResult(intent.createChooser(intent,"Tomar Foto"),1);
            }
        });

        boolean Es = new UtilsConnection().EstadoConex(this);
        Log.d("EstadoConx", Es+"");


        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("INFOCODE","ENTRANDO A LA ACTIVIDAD");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d("INFOCODE","TERMINANDO A LA ACTIVIDAD");
                super.onPostExecute(aVoid);

            }

            @Override
            protected Void doInBackground(Integer... params) {
                for (int i=0;i<10000;i++){
                    Log.d("INFOCODE","CONTINUANDO A LA ACTIVIDAD");
                }
                return null;
            }

        }.execute();
    }
}

