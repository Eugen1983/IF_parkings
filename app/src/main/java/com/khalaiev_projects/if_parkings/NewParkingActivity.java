package com.khalaiev_projects.if_parkings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewParkingActivity extends AppCompatActivity implements OnMapReadyCallback  {

    public static final String EXTRA_REPLY =
            "com.khalaiev_projects.if_parkings.REPLY";

    GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_parking);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_2);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = findViewById(R.id.fab_2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (googleMap != null) {
                    CameraPosition cameraPosition = googleMap.getCameraPosition();
                    LatLng currentCenter = cameraPosition.target;
                    replyIntent.putExtra("latitude", currentCenter.latitude);
                    replyIntent.putExtra("longitude", currentCenter.longitude);
                    setResult(RESULT_OK, replyIntent);
                }

                finish();
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        float zoom = intent.getFloatExtra("zoom", 0);
        Log.d(MyConstants.myLog, "" + zoom);

        LatLng startPosition = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, zoom));
    }
}
