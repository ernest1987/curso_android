package com.example.ernesto_ruiz1987.gps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGo = null;
    String geo = "";
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mGo == null){
            mGo= new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGo);
        if (location!=null){
            Geocoder gc= new Geocoder(this);
            List<Address> add=null;
            try {
                add = gc.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("info", location.getLatitude()+"");
            Log.d("info", location.getLongitude()+"");
            Log.d("info", add.get(0).getCountryCode()+"");
            Log.d("info", add.get(0).getAdminArea()+"");
            Log.d("info", add.get(0).getCountryName()+"");
            Uri Maps = Uri.parse("geo:"+location.getLatitude()+" "+location.getLongitude()+"?q=restaurant");
            Intent mi= new Intent(Intent.ACTION_VIEW,Maps);
            mi.setPackage("com.google.android.apps.maps");
            if (mi.resolveActivity(getPackageManager())!=null){
                startActivity(mi);
            }


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGo.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGo.disconnect();;
        super.onStop();
    }
}
