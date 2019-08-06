package com.triagung.moviecataloguefinal.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.triagung.moviecataloguefinal.LoadFavoriteTvShowCallback;

import java.lang.ref.WeakReference;

import static com.triagung.moviecataloguefinal.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV_SHOW;

public class LoadTvShowFavoriteAsync extends AsyncTask<Void, Void, Cursor> {
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadFavoriteTvShowCallback> weakCallback;

    public LoadTvShowFavoriteAsync(Context context, LoadFavoriteTvShowCallback callback) {
        weakContext = new WeakReference<>(context);
        weakCallback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakCallback.get().favoriteTvShowPreExecute();
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        Context context = weakContext.get();
        return context.getContentResolver().query(CONTENT_URI_TV_SHOW, null, null, null, null);
    }

    @Override
    protected void onPostExecute(Cursor tvShowItems) {
        super.onPostExecute(tvShowItems);
        weakCallback.get().favoriteTvShowPostExecute(tvShowItems);
    }
}
