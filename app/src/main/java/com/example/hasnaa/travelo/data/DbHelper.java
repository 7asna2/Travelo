package com.example.hasnaa.travelo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DbHelper extends SQLiteOpenHelper {


    static final String NAME = "favPlaces.db";
    private static final int VERSION = 1;


    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String builder = "CREATE TABLE " + Contract.PlaceInstance.TABLE_NAME + " (" +
                Contract.PlaceInstance.COLUMN_ID+ " TEXT PRIMARY KEY, " +
                Contract.PlaceInstance.COLUMN_NAME + " TEXT NOT NULL, " +
                Contract.PlaceInstance.COLUMN_RATING+ " REAL NOT NULL, " +
//                Quote.COLUMN_ABSOLUTE_CHANGE + " REAL NOT NULL, " +
//                Quote.COLUMN_PERCENTAGE_CHANGE + " REAL NOT NULL, " +
//                Quote.COLUMN_YEAR_HISTORY + " TEXT NOT NULL, " +
//                Quote.COLUMN_MONTH_HISTORY+ " TEXT NOT NULL, " +
                "UNIQUE (" + Contract.PlaceInstance.COLUMN_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(builder);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + Contract.PlaceInstance.TABLE_NAME);

        onCreate(db);
    }
}
