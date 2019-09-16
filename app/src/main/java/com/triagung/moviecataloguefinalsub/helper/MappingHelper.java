package com.triagung.moviecataloguefinalsub.helper;

import android.database.Cursor;

import com.triagung.moviecataloguefinalsub.database.DatabaseContract;
import com.triagung.moviecataloguefinalsub.model.Movie;
import com.triagung.moviecataloguefinalsub.model.TvShow;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayListMovieFavorite(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.BACKDROP));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DATE));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.YEAR));
            double rating= cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RATING));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW));

            movies.add(new Movie(id, backdrop, poster, title, date, year, rating, overview));
        }

        return movies;
    }

    public static ArrayList<TvShow> mapCursorToArrayListTvShowFavorite(Cursor cursor) {
        ArrayList<TvShow> tvShows = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.BACKDROP));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.POSTER));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.DATE));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.YEAR));
            double rating= cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.RATING));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.OVERVIEW));

            tvShows.add(new TvShow(id, backdrop, poster, title, date, year, rating, overview));
        }

        return tvShows;
    }

}
