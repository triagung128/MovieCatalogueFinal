package com.triagung.moviecataloguefinalsub.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.triagung.moviecataloguefinalsub.LoadFavoriteMovieCallback;
import com.triagung.moviecataloguefinalsub.asynctask.LoadMovieFavoriteAsync;

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
