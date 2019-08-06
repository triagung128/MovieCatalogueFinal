package com.triagung.moviecataloguefinal.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.triagung.moviecataloguefinal.LoadFavoriteTvShowCallback;
import com.triagung.moviecataloguefinal.asynctask.LoadTvShowFavoriteAsync;

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
