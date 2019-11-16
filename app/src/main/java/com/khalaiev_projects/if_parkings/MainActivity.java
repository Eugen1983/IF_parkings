package com.khalaiev_projects.if_parkings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int NEW_PARKING_ACTIVITY_REQUEST_CODE = 1;

    private ParkingViewModel mParkingViewModel;
    private Marker currentMarker;
    private int markersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(final GoogleMap googleMap) {

        LatLng startPosition = new LatLng(48.918620, 24.717910);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 13));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentMarker = marker;
                Log.d(MyConstants.myLog, "Current marker is changed to " + currentMarker);
                return false;
            }
        });

        mParkingViewModel = ViewModelProviders.of(this).get(ParkingViewModel.class);
        mParkingViewModel.getAllParkings().observe(this, new Observer<List<Parking>>() {
            @Override
            public void onChanged(@Nullable final List<Parking> parkings) {
                googleMap.clear();
                markersCount = 0;
                for (Parking parking : parkings) {
                    Log.d(MyConstants.myLog, "Parking with id = " + parking.getId() + ", latitude = "
                            + parking.getLatitude() + ", longitude = " + parking.getLongitude() + ", address = " + parking.getAddress());
                    LatLng position = new LatLng(parking.getLatitude(), parking.getLongitude());
                    MarkerOptions marker = new MarkerOptions().position(position).title(parking.getAddress());
                    googleMap.addMarker(marker).setTag(String.valueOf(parking.getId()));
                    Log.d(MyConstants.myLog, "Added markers: " + ++markersCount);
                }            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewParkingActivity.class);
                CameraPosition cameraPosition = googleMap.getCameraPosition();
                LatLng currentCenter = cameraPosition.target;
                intent.putExtra("latitude", currentCenter.latitude);
                intent.putExtra("longitude", currentCenter.longitude);
                intent.putExtra("zoom", cameraPosition.zoom);
                startActivityForResult(intent, NEW_PARKING_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.clear_data:
                mParkingViewModel.deleteAllParkings();
                break;
            case R.id.delete_marker:
                if (currentMarker != null) {
                    Log.d(MyConstants.myLog, "Deleting marker " + currentMarker);
                    int currentMarkerId = Integer.parseInt((String) currentMarker.getTag());
                    mParkingViewModel.deleteParking(currentMarkerId);
                    currentMarker = null;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PARKING_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            String address = getAddressFromLocation(latitude, longitude);
            Log.d(MyConstants.myLog, address);
            Parking parking = new Parking(latitude, longitude ,address);
            mParkingViewModel.insert(parking);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    private String getAddressFromLocation(double latitude, double longitude) {
        try {
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(this, new Locale.Builder().setLanguage("UKR").setScript("Latn").setRegion("UA").build());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String house = addresses.get(0).getFeatureName();
            String street = addresses.get(0).getThoroughfare();

            street = street.replace("вулиця ", "вул. ");

            if (!street.equals(house)) {
                house = ", " + house;
            }
            else {
                house = "";
            }

            String address = street + house;

            return address;

        } catch (IOException e) {e.printStackTrace();}
          catch (NullPointerException e) {}

        return "";
    }
}
