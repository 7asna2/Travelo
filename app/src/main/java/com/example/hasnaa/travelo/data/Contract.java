package com.example.hasnaa.travelo.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String AUTHORITY = "com.example.hasnaa.travelo";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String FAV_PLACES= "place";
    public static final String PLACES_BY_ID= "place/*";

    public static final class PlaceInstance implements BaseColumns {

        public static final Uri uri = BASE_URI.buildUpon().appendPath(FAV_PLACES).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RATING = "rating";
//        public static final String COLUMN_PERCENTAGE_CHANGE = "percentage_change";
//        public static final String COLUMN_YEAR_HISTORY = "year";
//        public static final String COLUMN_MONTH_HISTORY = "month";
//        public static final String COLUMN_WEEK_HISTORY = "week";


        public static final int POSITION_ID = 0;
        public static final int POSITION_NAME = 1;
        public static final int POSITION_RATING = 2;



        public static final String[] FAV_COLUMNS = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_RATING,
        };

        public static Uri makeUriForPlace(String id) {
            return uri.buildUpon().appendPath(id).build();
        }



        public static String getPlaceFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }
}
