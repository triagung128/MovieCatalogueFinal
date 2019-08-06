package com.triagung.moviecataloguefinal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.triagung.moviecataloguefinal.database.DatabaseContract.MovieColumns.TABLE_MOVIE;
import static com.triagung.moviecataloguefinal.database.DatabaseContract.TvShowColumns.TABLE_TV_SHOW;

public class FavoriteHelper {
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) databaseHelper.close();
    }

    public Cursor queryMovieFavorite() {
        return database.query(TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryTvShowFavorite() {
        return database.query(TABLE_TV_SHOW,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryByIdMovieFavorite(String id) {
        return database.query(TABLE_MOVIE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Cursor queryByIdTvShowFavorite(String id) {
        return database.query(TABLE_TV_SHOW,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public long insertMovieFavorite(ContentValues values) {
        return database.insert(TABLE_MOVIE, null, values);
    }

    public long insertTvShowFavorite(ContentValues values) {
        return database.insert(TABLE_TV_SHOW, null, values);
    }

    public int deleteMovieFavorite(String id) {
        return database.delete(TABLE_MOVIE, _ID + " = ?", new String[]{id});
    }

    public int deleteTvShowFavorite(String id) {
        return database.delete(TABLE_TV_SHOW, _ID + " = ?", new String[]{id});
    }
}
