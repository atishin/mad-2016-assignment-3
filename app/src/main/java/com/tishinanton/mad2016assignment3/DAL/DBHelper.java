package com.tishinanton.mad2016assignment3.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tishin Anton on 23.05.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static final String DB_NAME = "Assignment3";

    private static final String PLACES_TABLE = "Places";

    private static final String PLACES_FIELD_ID = "Id";
    private static final String PLACES_FIELD_LAT = "Lat";
    private static final String PLACES_FIELD_LNG = "Lng";
    private static final String PLACES_FIELD_TITLE = "Title";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + PLACES_TABLE + "(" +
                PLACES_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PLACES_FIELD_TITLE + " TEXT NOT NULL," +
                PLACES_FIELD_LAT + " REAL," +
                PLACES_FIELD_LNG + " REAL" +
                ")";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableSql = "DROP TABLE " + PLACES_TABLE;
        db.execSQL(dropTableSql);
        onCreate(db);
    }
}
