package com.example.ernesto_ruiz1987.curso_vie_08_07_2016;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ernesto_ruiz1987 on 08-07-16.
 */
public class base {

    static String ruta= "http://mindicador.cl/api";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get (String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {

        return ruta + relativeUrl;
    }
    
    public  void getDatos() throws JSONException {
        base.get("", null, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject firsEvent = null;
                Log.d("info", response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.d("error", throwable.toString());
            }
        });
    }

}
