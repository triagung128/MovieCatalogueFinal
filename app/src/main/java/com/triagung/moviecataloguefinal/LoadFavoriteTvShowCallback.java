package com.triagung.moviecataloguefinal;

import android.database.Cursor;

public interface LoadFavoriteTvShowCallback {
    void favoriteTvShowPreExecute();
    void favoriteTvShowPostExecute(Cursor tvShowItems);
}
