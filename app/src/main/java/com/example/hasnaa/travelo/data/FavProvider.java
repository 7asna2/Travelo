package com.example.hasnaa.travelo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class FavProvider extends ContentProvider {

    private final String TAG = this.getClass().getSimpleName();
    static final int FAV_PLACES = 100;
    static final int PLACE_BY_ID = 101;

    static UriMatcher uriMatcher = buildUriMatcher();

    private DbHelper dbHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.FAV_PLACES, FAV_PLACES);
        matcher.addURI(Contract.AUTHORITY, Contract.PLACES_BY_ID, PLACE_BY_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case FAV_PLACES:
                returnCursor = db.query(
                        Contract.PlaceInstance.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case PLACE_BY_ID:
                returnCursor = db.query(
                        Contract.PlaceInstance.TABLE_NAME,
                        projection,
                        Contract.PlaceInstance.COLUMN_ID + " = ?",
                        new String[]{Contract.PlaceInstance.getPlaceFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

//        if (db.isOpen()) {
//            db.close();
//        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        long b;

        switch (uriMatcher.match(uri)) {
            case FAV_PLACES:
                b =db.insert(
                        Contract.PlaceInstance.TABLE_NAME,
                        null,
                        values
                );
                returnUri = Contract.PlaceInstance.uri;
                break;
            default:
                Log.d(TAG , "error inserting new value ");
                throw new UnsupportedOperationException("Unknown URI:" + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(TAG , "insert return value "+b);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (uriMatcher.match(uri)) {
            case FAV_PLACES:
                rowsDeleted = db.delete(
                        Contract.PlaceInstance.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;

            case PLACE_BY_ID:
                String symbol = Contract.PlaceInstance.getPlaceFromUri(uri);
                rowsDeleted = db.delete(
                        Contract.PlaceInstance.TABLE_NAME,
                        '"' + symbol + '"' + " =" + Contract.PlaceInstance.COLUMN_ID,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case FAV_PLACES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        db.insert(
                                Contract.PlaceInstance.TABLE_NAME,
                                null,
                                value
                        );
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }


    }
}
