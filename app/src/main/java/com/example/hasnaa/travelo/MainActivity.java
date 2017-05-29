package com.example.hasnaa.travelo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.net.http.RequestQueue;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.example.hasnaa.travelo.DataRequest.GooglePlaceDetailRequest;
import com.example.hasnaa.travelo.DataRequest.GooglePlacesNearbyRequest;
import com.example.hasnaa.travelo.DataRequest.PhotoTask;
import com.example.hasnaa.travelo.data.Contract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , LocationListener,LoaderManager.LoaderCallbacks<Cursor>{
    private final String TAG = this.getClass().getSimpleName();

    private ProgressDialog pDialog;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Snackbar snackbar;
    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;

    @BindView(R.id.adView) AdView adView;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.fab) FloatingActionButton fab;

    public GoogleApiClient mGoogleApiClient;
    private GooglePlacesNearbyRequest nearbyRequest;

    private double cLat;
    private double cLng;
    private Location cLocation;
    private LocationManager mLocationManager;

    FavAdapter favAdapter;
    private static final int CURSOR_LOADER_ID = 0;


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            adView.loadAd(adRequest);
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        favAdapter = new FavAdapter(this,mGoogleApiClient, new FavAdapter.AdapterOnClickHandler() {
            @Override
            public void onClick(String id) {
                Intent intent = new Intent(getBaseContext(),Details.class);
                intent.putExtra("key",id);
                startActivity(intent);
            }

        });
        recyclerView.setAdapter(favAdapter);



//        getLocation();



//        nearbyRequest = new GooglePlacesNearbyRequest(this,mGoogleApiClient) {
//            GooglePlaceDetailRequest googlePlaceDetailRequest =  new GooglePlaceDetailRequest(getBaseContext(),mGoogleApiClient) {
//                @Override
//                public void update(PlaceBuffer places) {
//                }
//
//                @Override
//                public void updateList(ArrayList<Place> placeList) {
//
////                    Adapter adapter = new Adapter(placeList);
////                    mRecyclerView.setAdapter(adapter);
//                }
//            };
//            @Override
//            public void updateUponNearbyRequest(ArrayList<String> places) {
//                for(String id :places) {
//                    Log.d(TAG, "id:" + id);
//                    googlePlaceDetailRequest.reqPlaceById(places);
//                }
//
//            }
//
//            @Override
//            public void UpdateUponErrorNearbyRequest() {
//                Log.d(TAG,"failed");
//            }
//        };



//        if(getIntent()!=null){
//            if(cLocation !=null){
//                nearbyRequest.reqNearbyRestaurants(cLat,cLng,1000);
//
//            }
//        }




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddPlaceActivity.class);
                startActivity(intent);

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    public void getLocation() {
//        boolean isGPSEnabled, isNetworkEnabled;
//
//        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        // getting network status
//        isNetworkEnabled = mLocationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        if (isGPSEnabled && isNetworkEnabled) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,
//                    1, this);
//
//
//            if (mLocationManager != null) {
//                cLocation = mLocationManager
//                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                if(cLocation !=null) {
//                    cLng = cLocation.getLongitude();
//                    cLat = cLocation.getLatitude();
//                    Log.d(TAG, "not null ,lat: " + cLat + ",lng: " + cLng);
//                    if (snackbar != null && snackbar.isShown()) {
//                        snackbar.dismiss();
//
//                    }
//                }
//            } else {
//                snackbar = Snackbar
//                        .make(coordinatorLayout, "NO Network Connection", Snackbar.LENGTH_LONG)
//                        .setAction("retry", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                showSettingsAlert();
//                                getLocation();
//                            }
//                        });
//
//                snackbar.show();
//
//            }
//        }
//    }
//
//    public void showSettingsAlert(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//
//        alertDialog.setTitle("GPS settings");
//
//        alertDialog.setMessage("GPS is not enabled. Do you want enable it?");
//
//        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        });
//
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        alertDialog.show();
//    }

    @Override
    public void onLocationChanged(Location location) {

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

////////////////////////////////////////////loader manager//////////////////////////////
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,Contract.PlaceInstance.uri, Contract.PlaceInstance.FAV_COLUMNS
                ,null,null,Contract.PlaceInstance.COLUMN_ID);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // @TODO : make error view and set its text to error
//        if (data.getCount() != 0) {
//            error.setVisibility(View.GONE);
//        }
        favAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favAdapter.setCursor(null);
    }
}
