package com.triagung.moviecataloguefinalsub;

import android.database.Cursor;

public interface LoadFavoriteTvShowCallback {
    void favoriteTvShowPreExecute();
    void favoriteTvShowPostExecute(Cursor tvShowItems);
}
