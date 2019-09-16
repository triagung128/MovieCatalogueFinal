package com.triagung.moviecataloguefinalsub.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.triagung.moviecataloguefinalsub.LoadFavoriteTvShowCallback;
import com.triagung.moviecataloguefinalsub.asynctask.LoadTvShowFavoriteAsync;

public class DataTvShowObserver extends ContentObserver {
    private final Context context;

    public DataTvShowObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        new LoadTvShowFavoriteAsync(context, (LoadFavoriteTvShowCallback) context).execute();
    }
}
