package com.example.luis.tarea1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent( Logo.this, Login.class );
                startActivity( intent );
            }
        };

        Timer timer = new Timer();
        timer.schedule( task , 2000 );

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("INFOCODE", "Actividad en resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("INFOCODE", "Actividad en pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("INFOCODE", "Actividad en destroy");
    }
}
