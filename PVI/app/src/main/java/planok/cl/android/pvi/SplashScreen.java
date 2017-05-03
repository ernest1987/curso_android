package planok.cl.android.pvi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

    //Duracion del splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        prefs = getSharedPreferences("Preferencias_PVI", Context.MODE_PRIVATE);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                boolean ingreso = prefs.getBoolean("ingresado", false);
                Intent mainIntent;
                if(ingreso)
                    mainIntent = new Intent().setClass(
                            SplashScreen.this, MenuInicial.class);
                else
                    mainIntent = new Intent().setClass(
                            SplashScreen.this, LoginKey.class);
                //Abrir nueva actividad y finalizar la actual
                startActivity(mainIntent);
                finish();
            }
        };

        // Simula tiempo de carga
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
