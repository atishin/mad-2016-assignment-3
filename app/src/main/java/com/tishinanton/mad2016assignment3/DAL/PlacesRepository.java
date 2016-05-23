package com.tishinanton.mad2016assignment3.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Tishin Anton on 23.05.2016.
 */
public class PlacesRepository {

    private Context context;

    public PlacesRepository(Context context) {
        this.context = context;
    }

    public ArrayList<Place> getAll() {
        DBHelper dbHelper = new DBHelper(context);
        ArrayList<Place> places = new ArrayList<>();
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.PLACES_TABLE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Place place = new Place();
                    place.Id = cursor.getInt(cursor.getColumnIndex(DBHelper.PLACES_FIELD_ID));
                    place.Title = cursor.getString(cursor.getColumnIndex(DBHelper.PLACES_FIELD_TITLE));
                    place.Lat = cursor.getDouble(cursor.getColumnIndex(DBHelper.PLACES_FIELD_LAT));
                    place.Lng = cursor.getDouble(cursor.getColumnIndex(DBHelper.PLACES_FIELD_LNG));
                    places.add(place);
                } while (cursor.moveToNext());
            }
        }
        finally {
            dbHelper.close();
        }
        return places;
    }

    public Place addPlace(String title, double lat, double lng) {
        DBHelper dbHelper = new DBHelper(context);
        Place place = new Place();
        place.Title = title;
        place.Lat = lat;
        place.Lng = lng;
        place.Id = -1;
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues placeValues = new ContentValues();
            placeValues.put(DBHelper.PLACES_FIELD_TITLE, title);
            placeValues.put(DBHelper.PLACES_FIELD_LAT, lat);
            placeValues.put(DBHelper.PLACES_FIELD_LNG, lng);
            place.Id = (int) db.insert(DBHelper.PLACES_TABLE, null, placeValues);
        } finally {
            dbHelper.close();
        }
        if (place.Id == -1) {
            return null;
        } else {
            return place;
        }
    }

}
