package com.example.hasnaa.travelo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.example.hasnaa.travelo.DataRequest.GooglePlacesNearbyRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddPlaceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener{


    private final String TAG = this.getClass().getSimpleName();
    private Snackbar snackbar;

    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;

    private GooglePlacesNearbyRequest nearbyRequest;
    @BindView(R.id.restaurants) CardView restaurants;

    @BindView(R.id.entertainment) CardView entertainment;

    @BindView(R.id.hotels)
    CardView hotels;

    @BindView(R.id.toolbar) Toolbar toolbar;

    ArrayList<String> searchPlaces;


    private double cLat;
    private double cLng;
    private Location cLocation;


    private LocationManager mLocationManager;
//    private GooglePlacesNearbyRequest nearbyRequest;

    private GoogleApiClient mGoogleApiClient;
    PlaceAutocompleteFragment autocompleteFragment;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        restaurants.setOnClickListener(this);
        hotels.setOnClickListener(this);
        entertainment.setOnClickListener(this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        getLocation();

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());

                Intent intent = new Intent(getBaseContext(),Details.class);
                intent.putExtra(Intent.EXTRA_TEXT,place.getId());
                startActivity(intent);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);

            }
        });


//        placePhotosTask();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


//    private void placePhotosTask() {
//        final String placeId = "ChIJrTLr-GyuEmsRBfy61i59si0"; // Australian Cruise Group
//
//        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
//        new PhotoTask(500, 500, mGoogleApiClient) {
//            @Override
//            protected void onPreExecute() {
//                // Display a temporary image to show while bitmap is loading.
////                mImageView.setImageResource(R.drawable.empty_photo);
//            }
//
//            @Override
//            protected void onPostExecute(AttributedPhoto attributedPhoto) {
//                if (attributedPhoto != null) {
//                    // Photo has been loaded, display it.
//                    mImageView.setImageBitmap(attributedPhoto.bitmap);
//
//                    // Display the attribution as HTML content if set.
//                    if (attributedPhoto.attribution == null) {
//                        textView.setVisibility(View.GONE);
//                    } else {
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
//                    }
//
//                }
//            }
//        }.execute(placeId);
//    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //*********************** LOCATION *******************************//
    public void getLocation() {
        boolean isGPSEnabled, isNetworkEnabled;

        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled && isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,
                    1, this);

            if (mLocationManager != null) {
                cLocation = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(cLocation !=null) {
                    cLng = cLocation.getLongitude();
                    cLat = cLocation.getLatitude();
                    Log.d(TAG, "not null ,lat: " + cLat + ",lng: " + cLng);
                    if (snackbar != null && snackbar.isShown()) {
                        snackbar.dismiss();

                    }
                }
            } else {
                snackbar = Snackbar
                        .make(coordinatorLayout, "NO Network Connection", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showSettingsAlert();
                                getLocation();
                            }
                        });

                snackbar.show();

            }
        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS settings");

        alertDialog.setMessage("GPS is not enabled. Do you want enable it?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        cLat = location.getLatitude();
        cLng = location.getLongitude();

        Log.d(TAG,"lat: "+cLat+",lng: "+cLng);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.restaurants){
            if(cLocation !=null) {
                nearbyRequest = new GooglePlacesNearbyRequest(this,mGoogleApiClient) {
                    @Override
                    public void updateUponNearbyRequest(ArrayList<String> places) {
                        Intent intent = new Intent(getBaseContext() , SearchActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT,places);
                        startActivity(intent);
                    }

                    @Override
                    public void UpdateUponErrorNearbyRequest() {
                        Log.d(TAG,"failed");

                    }
                };
                nearbyRequest.reqNearbyRestaurants(cLat, cLng, 1000);
            }
        }
        if(v.getId()== R.id.hotels){
            if(cLocation !=null) {
                nearbyRequest = new GooglePlacesNearbyRequest(this,mGoogleApiClient) {
                    @Override
                    public void updateUponNearbyRequest(ArrayList<String> places) {
                        Intent intent = new Intent(getBaseContext() , SearchActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT,places);
                        startActivity(intent);
                    }

                    @Override
                    public void UpdateUponErrorNearbyRequest() {
                        Log.d(TAG,"failed");

                    }
                };
                nearbyRequest.reqNearbyHotels(cLat, cLng, 1000);
            }
        }
        if(v.getId()== R.id.entertainment){
            if(cLocation !=null) {
                nearbyRequest = new GooglePlacesNearbyRequest(this,mGoogleApiClient) {
                    @Override
                    public void updateUponNearbyRequest(ArrayList<String> places) {
                        Intent intent = new Intent(getBaseContext() , SearchActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT,places);
                        startActivity(intent);
                    }

                    @Override
                    public void UpdateUponErrorNearbyRequest() {
                        Log.d(TAG,"failed");

                    }
                };
                nearbyRequest.reqNearbyInternments(cLat, cLng, 1000);
            }
        }
    }
}
