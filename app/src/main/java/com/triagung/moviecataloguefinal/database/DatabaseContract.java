package com.triagung.moviecataloguefinal.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.triagung.moviecataloguefinal";
    private static final String SCHEME = "content";

    public static final class MovieColumns implements BaseColumns {
        public static final String TABLE_MOVIE = "tb_movie_favorite";
        public static final String BACKDROP = "backdrop";
        public static final String POSTER = "poster";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String YEAR = "year";
        public static final String RATING = "rating";
        public static final String OVERVIEW = "overview";

        public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }

    public static final class TvShowColumns implements BaseColumns {
        public static final String TABLE_TV_SHOW = "tb_tv_show_favorite";
        public static final String BACKDROP = "backdrop";
        public static final String POSTER = "poster";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String YEAR = "year";
        public static final String RATING = "rating";
        public static final String OVERVIEW = "overview";

        public static final Uri CONTENT_URI_TV_SHOW = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TV_SHOW)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }
}
