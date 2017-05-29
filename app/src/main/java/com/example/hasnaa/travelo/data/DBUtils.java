package com.example.hasnaa.travelo.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;



/**
 * Created by hasna2 on 26/01/2017.
 */
public class DBUtils {
    private final static String TAG = "DB";


//    [33, MSFT, 64, -0.06, -0.09, 1486357200000, 64.00

//    public static ArrayList<String> getStock(Context context , String id) {
//        ArrayList<String> al = new ArrayList<>();
//        Uri uri=Contract.PlaceInstance.makeUriForPlace(id);
//        Cursor cur = context.getContentResolver().query(uri,null,null,Contract.PlaceInstance.FAV_COLUMNS,null);
//        try {
//
//            if (cur.getCount() > 0) {
//                cur.moveToFirst();
//                al.add(cur.getString(Contract.PlaceInstance.POSITION_ID));
//                al.add(cur.getString(Contract.PlaceInstance.POSITION_NAME));
//                al.add(cur.getString(Contract.PlaceInstance.POSITION_RATING));
//                Log.d(TAG,"xxx :" + al.toString());
//
//            }
//        }catch (NullPointerException e){
//            Log.e(TAG,e.getMessage()+"cursor doesnt load ");
//            return null;
//        }
//        return al;
//    }







}



