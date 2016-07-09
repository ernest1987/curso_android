package com.example.ernesto_ruiz1987.curso_vie_08_07_2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        base b = new base();
        try {
            b.getDatos();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
