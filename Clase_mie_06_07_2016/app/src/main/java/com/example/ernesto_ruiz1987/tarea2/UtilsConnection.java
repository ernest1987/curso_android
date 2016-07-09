package com.example.ernesto_ruiz1987.tarea2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ernesto_ruiz1987 on 06-07-16.
 */
public class UtilsConnection {

    public boolean EstadoConex(Context context){

        ConnectivityManager Cm= (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo Estado= Cm.getActiveNetworkInfo();
        return Estado != null && Estado.isConnected();

    }


}
