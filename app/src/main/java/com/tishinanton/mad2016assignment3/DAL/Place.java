package com.tishinanton.mad2016assignment3.DAL;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tishin Anton on 23.05.2016.
 */
public class Place {
    public int Id;
    public String Title;
    public double Lat;
    public double Lng;

    public Place(int Id, String Title, double Lat, double Lng) {
        this.Id = Id;
        this.Title = Title;
        this.Lat = Lat;
        this.Lng = Lng;
    }

    public Place() {

    }

    public LatLng getLatLng() {
        return new LatLng(Lat, Lng);
    }
}
