package com.example.hasnaa.travelo;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasnaa.travelo.DataRequest.GooglePlaceDetailRequest;
import com.example.hasnaa.travelo.DataRequest.PhotoTask;
import com.example.hasnaa.travelo.data.Contract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private View rootView;
    private TextView placeNameView;
    private ImageView imageView;
    private Toolbar toolbar;
    private CoordinatorLayout mDrawInsetsFrameLayout;
    private NestedScrollView nestedScrollView;
    private CollapsingToolbarLayout collapsingToolbar;
    TextView bodyView;

    String pName;
    String pID;
    String pRating;
    LatLng pLatLong;


    @BindView(R.id.add_to_fav)Button addToFav;
    @BindView(R.id.map)Button viewOnMap;

    private String placeId;
    private GooglePlaceDetailRequest googlePlaceDetailRequest;
    private GoogleApiClient mGoogleApiClient;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this,rootView);
        bindViews(rootView);
        placeId =getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        bindViews(rootView);

        googlePlaceDetailRequest= new GooglePlaceDetailRequest(getContext(),mGoogleApiClient) {
            @Override
            public void update(PlaceBuffer places) {
                Place myPlace=places.get(0);
                if(myPlace != null) {
                    placeNameView.setText(myPlace.getName());
                    bodyView.setText(myPlace.getAddress());
                    placePhotosTask(placeId);
                    pID=placeId;
                    pName= (String) myPlace.getName();
                    pRating=myPlace.getRating()+"";
                    pLatLong=myPlace.getLatLng();
                }

            }

            @Override
            public void updateList(ArrayList<Place> placeList) {
            }
        };
        googlePlaceDetailRequest.reqPlaceById(placeId);




        return rootView;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.PlaceInstance.COLUMN_ID, placeId);
                contentValues.put(Contract.PlaceInstance.COLUMN_NAME, pName);
                contentValues.put(Contract.PlaceInstance.COLUMN_RATING, pRating);
                getActivity().getContentResolver()
                        .insert(Contract.PlaceInstance.uri, contentValues);
            }
        });
        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = String.format("geo:%f,%f", pLatLong.latitude, pLatLong.longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
            }
        });
    }


    private void bindViews(View rootView){
        mDrawInsetsFrameLayout = (CoordinatorLayout) rootView.findViewById(R.id.draw_insets_frame_layout);
        nestedScrollView =(NestedScrollView) rootView.findViewById(R.id.nestedScrollView);


        imageView = (ImageView) rootView.findViewById(R.id.photo);
        collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingToolbar);
        toolbar  = (Toolbar)rootView.findViewById(R.id.toolbar) ;
//        toolbar.setNavigationIcon(getActivity().getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        if(nestedScrollView!=null)
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == 0) toolbar.setBackgroundColor(Color.TRANSPARENT);
                    if (scrollY > oldScrollY) {
                        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
                    }

                }
            });

        rootView.setVisibility(View.VISIBLE);
        placeNameView = (TextView) rootView.findViewById(R.id.place_name);
        placeNameView.setMovementMethod(new LinkMovementMethod());
        bodyView = (TextView) rootView.findViewById(R.id.place_description);
    }


    private void placePhotosTask(String placeId) {
//        final String placeId = "ChIJrTLr-GyuEmsRBfy61i59si0"; // Australian Cruise Group

        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask(500, 500, mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
//                mImageView.setImageResource(R.drawable.empty_photo);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    imageView.setImageBitmap(attributedPhoto.bitmap);
                    // Display the attribution as HTML content if set.
//                    if (attributedPhoto.attribution == null) {
//                        textView.setVisibility(View.GONE);
//                    } else {
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
//                    }
                }
            }
        }.execute(placeId);
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
