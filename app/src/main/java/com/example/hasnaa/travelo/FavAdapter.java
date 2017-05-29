package com.example.hasnaa.travelo;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasnaa.travelo.DataRequest.PhotoTask;
import com.example.hasnaa.travelo.data.Contract;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hasnaa on 5/22/2017.
// */
//
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private Cursor cursor;
    private GoogleApiClient mGoogleApiClient;
    Context context;
    AdapterOnClickHandler clickHandler ;

    public FavAdapter(Context context,GoogleApiClient mGoogleApiClient, AdapterOnClickHandler clickHandler) {
        this.context=context;
        this.clickHandler=clickHandler;
        this.mGoogleApiClient = mGoogleApiClient;
    }

    void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.place_list_item, parent, false);
        final ViewHolder vh = new ViewHolder(item);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        cursor.moveToPosition(position);
        holder.titleView.setText(cursor.getString(Contract.PlaceInstance.POSITION_NAME));
        holder.subtitleView.setText(cursor.getString(Contract.PlaceInstance.POSITION_RATING));
        new PhotoTask(500, 500, mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    holder.thumbnailView.setImageBitmap(attributedPhoto.bitmap);
                }
            }
        }.execute(cursor.getString(Contract.PlaceInstance.POSITION_ID));
//        holder.thumbnailView.setAspectRatio(1);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;
    }

    interface AdapterOnClickHandler {
        void onClick(String id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

//        @BindView(R.id.thumbnail)  DynamicHeightNetworkImageView thumbnailView;
        @BindView(R.id.thumbnail) ImageView thumbnailView;

        @BindView(R.id.article_title)
        public TextView titleView;

        @BindView(R.id.article_subtitle)
        public TextView subtitleView;

        public int position;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
//            int symbolColumn = cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
            clickHandler.onClick(cursor.getString(Contract.PlaceInstance.POSITION_ID));
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////





