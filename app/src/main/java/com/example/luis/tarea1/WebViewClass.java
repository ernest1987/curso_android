package com.example.luis.tarea1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_web_view);

        WebView webView = (WebView) this.findViewById(R.id.webViewPage1);
        webView.loadUrl("http://www.falconmasters.com");
    }
}
