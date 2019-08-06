package com.triagung.moviecataloguefinal;

import android.database.Cursor;

public interface LoadFavoriteMovieCallback {
    void favoriteMoviePreExecute();
    void favoriteMoviePostExecute(Cursor movieItems);
}
