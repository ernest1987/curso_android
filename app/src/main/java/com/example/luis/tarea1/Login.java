package com.example.luis.tarea1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class Login extends AppCompatActivity {

    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] patron= {500,100,1000,250};
        vi.vibrate(patron,-1);
        Uri noti2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(this,noti2);
        r.play();

        Intent noti= new Intent(this,WebView.class);
        Notification.Builder NT = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Mensaje Pruba").setContentText("aqui va el mensaje");
        PendingIntent ci =PendingIntent.getActivity(this,R.mipmap.ic_launcher,noti,PendingIntent.FLAG_UPDATE_CURRENT);
        NT.setContentIntent(ci);

        NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,NT.build());

        btnAgregar = (Button) findViewById( R.id.btnIngresar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Login.this, WebViewClass.class );
                startActivity( intent );
            }
        });
    }
}
