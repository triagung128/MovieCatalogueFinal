package com.triagung.moviecataloguefinal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.triagung.moviecataloguefinal.database.DatabaseContract.MovieColumns;
import com.triagung.moviecataloguefinal.database.DatabaseContract.TvShowColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_favorite";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s DOUBLE NOT NULL, " +
                    "%s TEXT NOT NULL)",
            MovieColumns.TABLE_MOVIE,
            MovieColumns._ID,
            MovieColumns.BACKDROP,
            MovieColumns.POSTER,
            MovieColumns.TITLE,
            MovieColumns.DATE,
            MovieColumns.YEAR,
            MovieColumns.RATING,
            MovieColumns.OVERVIEW
    );

    private static final String SQL_CREATE_TABLE_TV_SHOW = String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s DOUBLE NOT NULL, " +
                    "%s TEXT NOT NULL)",
            TvShowColumns.TABLE_TV_SHOW,
            TvShowColumns._ID,
            TvShowColumns.BACKDROP,
            TvShowColumns.POSTER,
            TvShowColumns.TITLE,
            TvShowColumns.DATE,
            TvShowColumns.YEAR,
            TvShowColumns.RATING,
            TvShowColumns.OVERVIEW
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV_SHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieColumns.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TvShowColumns.TABLE_TV_SHOW);
        onCreate(db);
    }
}
