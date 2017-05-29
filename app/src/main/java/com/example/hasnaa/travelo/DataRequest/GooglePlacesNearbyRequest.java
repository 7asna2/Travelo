package com.example.hasnaa.travelo.DataRequest;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hasnaa.travelo.BuildConfig;
import com.example.hasnaa.travelo.R;
import com.example.hasnaa.travelo.Utils.AppController;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hasnaa on 4/29/2017.
 */
public abstract class GooglePlacesNearbyRequest {
    private final String TAG = this.getClass().getSimpleName();

    public static final String NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";


    public final int TYPE_RESTAURANTS_AND_CAFES=0;
    public final int TYPE_ENTERTAINMENT=1;
    public final int TYPE_HOTELS_AND_LODGING =2;


// *******************RESTAURANTS AND CAFES*************//
    public final String TYPE_RESTAURANT="restaurant";
    public final String TYPE_FOOD="food";
    public final String TYPE_CAFE="cafe";
    public final String TYPE_MEAL_TAKEAWAY="meal_takeaway";
    public final String TYPE_BAKERY="bakery";

//********************ENTERTAINMENT**********************//
    private final String TYPE_AMUSEMENT_PARK="amusement_park";
    private final String TYPE_AQUARIUM = "aquarium";
    private final String TYPE_ART_GALLERY="art_gallery";
    private final String TYPE_CAMP_GROUND = "campground";
    private final String TYPE_CASINO = "casino";
    private final String TYPE_MOVIE_THEATER = "movie_theater";
    private final String TYPE_MOVIE_RENTIAL = "movie_rential";
    private final String TYPE_MUSEUM = "museum";
    private final String TYPE_NIGHT_CLUB = "night_club";
    private final String TYPE_PARK = "park";
    private final String TYPE_ZOO="zoo";

//**************************HOTELS**********************//

    private final String TYPE_LODGING="lodging";



    Context context ;
//    GoogleApiClient googleApiClient;
    public GooglePlacesNearbyRequest (Context context , GoogleApiClient googleApiClient){
        this.context = context;
//        this.googleApiClient= googleApiClient;
    }







    public void reqNearbyInternments (final double lat, final double lon, final int radius){
        reqNearby(lat,lon,radius,TYPE_ENTERTAINMENT);
    }
    public void reqNearbyRestaurants (final double lat, final double lon, final int radius){
        reqNearby(lat,lon,radius,TYPE_RESTAURANTS_AND_CAFES);
    }
    public void reqNearbyHotels (final double lat, final double lon, final int radius){
        reqNearby(lat,lon,radius,TYPE_HOTELS_AND_LODGING );
    }



    public void reqNearby(final double lat, final double lon, final int radius , int type){
        String typeParam="";
        switch (type) {
            case TYPE_ENTERTAINMENT:
                typeParam = TYPE_AMUSEMENT_PARK + '|' +
                        TYPE_AQUARIUM + '|' +
                        TYPE_ART_GALLERY + '|' +
                        TYPE_CAMP_GROUND + "|" +
                        TYPE_CASINO + '|' +
                        TYPE_MOVIE_THEATER + '|' +
                        TYPE_MOVIE_RENTIAL + '|' +
                        TYPE_MUSEUM + '|' +
                        TYPE_NIGHT_CLUB + '|' +
                        TYPE_PARK + '|' +
                        TYPE_ZOO;
                break;
            case TYPE_RESTAURANTS_AND_CAFES:
                typeParam = TYPE_RESTAURANT + '|' +
                        TYPE_FOOD + '|' +
                        TYPE_CAFE + '|' +
                        TYPE_MEAL_TAKEAWAY + '|' +
                        TYPE_BAKERY;
                break;
            case TYPE_HOTELS_AND_LODGING:
                typeParam = TYPE_LODGING;
                break;
            default:
                typeParam = "";

        }
        Uri.Builder builder = Uri.parse(NEARBY_SEARCH_URL).buildUpon();
        builder.appendQueryParameter("key", BuildConfig.GOOGLE_PLACES_API_KEY);
        builder.appendQueryParameter("location", lat+ "," +lon);
        builder.appendQueryParameter("types",typeParam);
        builder.appendQueryParameter("radius",  String.valueOf(radius));
        String loginUrl=builder.build().toString();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                loginUrl ,null,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                updateUponNearbyRequest(new Parser().parse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UpdateUponErrorNearbyRequest();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };
        Log.d(TAG ,jsonObjReq.getUrl());
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    class Parser{
        final String RESULTS="results";
        final String PLACE_ID="place_id";
        public ArrayList<String> parse (JSONObject jsonObject){
            ArrayList<String> places = new ArrayList<>();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    String id = o.getString(PLACE_ID);
                    places.add(id);
                }
            }catch (JSONException e){
                Log.e(TAG , e.getMessage());
            }
        return places;
        }
    }

    public abstract void updateUponNearbyRequest(ArrayList<String> places);
    public abstract void UpdateUponErrorNearbyRequest();
}
