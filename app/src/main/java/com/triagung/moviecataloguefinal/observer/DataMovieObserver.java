package com.triagung.moviecataloguefinal.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.triagung.moviecataloguefinal.LoadFavoriteMovieCallback;
import com.triagung.moviecataloguefinal.asynctask.LoadMovieFavoriteAsync;

public class DataMovieObserver extends ContentObserver {
    private final Context context;

    public DataMovieObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        new LoadMovieFavoriteAsync(context, (LoadFavoriteMovieCallback) context).execute();
    }
}
