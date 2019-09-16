package com.triagung.moviecataloguefinalsub.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.triagung.moviecataloguefinalsub.LoadFavoriteMovieCallback;

import java.lang.ref.WeakReference;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class LoadMovieFavoriteAsync extends AsyncTask<Void, Void, Cursor> {
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadFavoriteMovieCallback> weakCallback;

    public LoadMovieFavoriteAsync(Context context, LoadFavoriteMovieCallback callback) {
        weakContext = new WeakReference<>(context);
        weakCallback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakCallback.get().favoriteMoviePreExecute();
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        Context context = weakContext.get();
        return context.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
    }

    @Override
    protected void onPostExecute(Cursor movieItems) {
        super.onPostExecute(movieItems);
        weakCallback.get().favoriteMoviePostExecute(movieItems);
    }
}
