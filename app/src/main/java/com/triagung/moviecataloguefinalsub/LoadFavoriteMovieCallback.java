package com.triagung.moviecataloguefinalsub;

import android.database.Cursor;

public interface LoadFavoriteMovieCallback {
    void favoriteMoviePreExecute();
    void favoriteMoviePostExecute(Cursor movieItems);
}
