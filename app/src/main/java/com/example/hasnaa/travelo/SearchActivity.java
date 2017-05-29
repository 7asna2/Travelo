package com.example.hasnaa.travelo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.hasnaa.travelo.DataRequest.GooglePlaceDetailRequest;
import com.example.hasnaa.travelo.Details;
import com.example.hasnaa.travelo.NearbySearchAdapter;
import com.example.hasnaa.travelo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;



    @BindView(R.id.toolbar) Toolbar toolbar;

    ArrayList<Place> searchPlaces;
    ArrayList<String>ids;
    NearbySearchAdapter nearbySearchAdapter;
    public GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        Intent intent = getIntent();
        if(intent != null) {
//            Bundle args = intent.getBundleExtra("BUNDLE");
//            ids = (ArrayList<String>) args.getSerializable("ARRAYLIST");
            ids= intent.getStringArrayListExtra(Intent.EXTRA_TEXT);


            nearbySearchAdapter = new NearbySearchAdapter(this, ids, new NearbySearchAdapter.OnClickHandler() {
                @Override
                public void onClick(String placeId) {
                    Intent intent = new Intent(getBaseContext(), Details.class);
                    intent.putExtra("key", placeId);
                    startActivity(intent);
                }


            }, mGoogleApiClient);
        }
        recyclerView.setAdapter(nearbySearchAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
