package com.example.hasnaa.travelo.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.hasnaa.travelo.R;
import com.example.hasnaa.travelo.data.Contract;

/**
 * Created by Hasnaa on 5/27/2017.
 */
public class WidgetService  extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                Uri uri = Contract.PlaceInstance.uri;
                data = getContentResolver().query(uri
                        , Contract.PlaceInstance.FAV_COLUMNS
                        , null, null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews view = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);

                view.setTextViewText(R.id.courseName, data.getString(
                        data.getColumnIndex(Contract.PlaceInstance.COLUMN_NAME)));

                final Intent fillInIntent = new Intent();
                view.setOnClickFillInIntent(R.id.line1, fillInIntent);

                return view;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {

//                if (data.moveToPosition(i)) {
//                            data.getString(data.getColumnIndex(DataContract.CourseEntry._ID));
//                    return l;
//                }
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}

