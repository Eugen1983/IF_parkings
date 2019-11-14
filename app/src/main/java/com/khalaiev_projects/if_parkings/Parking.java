package com.khalaiev_projects.if_parkings;

import com.google.android.gms.maps.model.LatLng;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parkings_table")
public class Parking {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private double latitude;

    private double longitude;

    private String address;

    public Parking(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}