package com.triagung.moviecataloguefinalsub.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.model.Movie;

import java.util.concurrent.ExecutionException;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private Cursor cursor;

    StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long identifyToken = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);

        Binder.restoreCallingIdentity(identifyToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() > 0) {
            Movie movie = getItem(position);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

            try {
                Bitmap favoriteImageBitmap = Glide.with(context).asBitmap().load("https://image.tmdb.org/t/p/w342/" + movie.getPoster()).submit().get();
                remoteViews.setImageViewBitmap(R.id.img_widget, favoriteImageBitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Bundle extras = new Bundle();
            extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            remoteViews.setOnClickFillInIntent(R.id.img_widget, fillInIntent);
            return remoteViews;

        } else {
            return new RemoteViews(context.getPackageName(), R.layout.widget_item);
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)){
            throw new IllegalStateException("Position invalid!");
        }

        return new Movie(cursor);
    }
}
