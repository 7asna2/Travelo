package com.example.hasnaa.travelo.DataRequest;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

/**
 * Created by Hasnaa on 5/19/2017.
 */
public abstract class GooglePlaceDetailRequest {

    private final String TAG = this.getClass().getSimpleName();

    private CoordinatorLayout mDrawInsetsFrameLayout;
    private Toolbar toolbar;
    private NestedScrollView nestedScrollView;
    private CollapsingToolbarLayout collapsingToolbar ;

    private Context context;
    private GoogleApiClient googleApiClient;

    public GooglePlaceDetailRequest (Context context , GoogleApiClient googleApiClient){
        this.context = context;
        this.googleApiClient= googleApiClient;
    }



    public void reqPlaceById (String placeId){
        Places.GeoDataApi.getPlaceById(googleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            update(places);
                            Log.i(TAG, "Place found: " + places.get(0).getName());
                        } else {
                            Log.e(TAG, "Place not found");
                        }
                        places.release();
                    }
                });
    }

//    public void reqPlaceById() {
//        final ArrayList<Place> placesList = new ArrayList<>();
//
//
//        new AsyncTask<String, Void, Void>() {
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                updateList(placesList);
//                super.onPostExecute(aVoid);
//            }
//
//            @Override
//            protected Void doInBackground(String... params) {
//                for (String id : placesId) {
//                    Places.GeoDataApi.getPlaceById(googleApiClient, id)
//                            .setResultCallback(new ResultCallback<PlaceBuffer>() {
//                                @Override
//                                public void onResult(PlaceBuffer places) {
//                                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
//                                        placesList.add(places.get(0));
//                                        Log.i(TAG, "Place found: " + places.get(0).getName());
//                                    } else {
//                                        Log.e(TAG, "Place not found");
//                                    }
//                                    places.release();
//                                }
//                            });
//                }
//                return null;
//            }
//        }.execute(new String());
//    }

    abstract public void update (PlaceBuffer places);
    abstract public void updateList (ArrayList <Place> placeList);
}
