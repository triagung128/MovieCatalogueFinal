package com.triagung.moviecataloguefinal.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.triagung.moviecataloguefinal.database.FavoriteHelper;
import com.triagung.moviecataloguefinal.observer.DataMovieObserver;
import com.triagung.moviecataloguefinal.observer.DataTvShowObserver;

import java.util.Objects;

import static com.triagung.moviecataloguefinal.database.DatabaseContract.AUTHORITY;
import static com.triagung.moviecataloguefinal.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;
import static com.triagung.moviecataloguefinal.database.DatabaseContract.MovieColumns.TABLE_MOVIE;
import static com.triagung.moviecataloguefinal.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV_SHOW;
import static com.triagung.moviecataloguefinal.database.DatabaseContract.TvShowColumns.TABLE_TV_SHOW;

public class FavoriteProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV_SHOW = 3;
    private static final int TV_SHOW_ID = 4;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE + "/#", MOVIE_ID);

        sUriMatcher.addURI(AUTHORITY, TABLE_TV_SHOW, TV_SHOW);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV_SHOW + "/#", TV_SHOW_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoriteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favoriteHelper.queryMovieFavorite();
                break;
            case MOVIE_ID:
                cursor = favoriteHelper.queryByIdMovieFavorite(uri.getLastPathSegment());
                break;
            case TV_SHOW:
                cursor = favoriteHelper.queryTvShowFavorite();
                break;
            case TV_SHOW_ID:
                cursor = favoriteHelper.queryByIdTvShowFavorite(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favoriteHelper.open();
        long added;
        Uri favoriteUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = favoriteHelper.insertMovieFavorite(values);
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI_MOVIE, new DataMovieObserver(new Handler(), getContext()));
                favoriteUri = Uri.parse(CONTENT_URI_MOVIE + "/" + added);
                break;
            case TV_SHOW:
                added = favoriteHelper.insertTvShowFavorite(values);
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI_TV_SHOW, new DataTvShowObserver(new Handler(), getContext()));
                favoriteUri = Uri.parse(CONTENT_URI_TV_SHOW + "/" + added);
                break;
            default:
                added = 0;
                favoriteUri = null;
                break;
        }
        return favoriteUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoriteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = favoriteHelper.deleteMovieFavorite(uri.getLastPathSegment());
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI_MOVIE, new DataMovieObserver(new Handler(), getContext()));
                break;
            case TV_SHOW_ID:
                deleted = favoriteHelper.deleteTvShowFavorite(uri.getLastPathSegment());
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI_TV_SHOW, new DataTvShowObserver(new Handler(), getContext()));
                break;
            default:
                deleted = 0;
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
