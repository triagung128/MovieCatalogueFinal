package com.triagung.moviecataloguefinalsub.activity;

import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.triagung.moviecataloguefinalsub.LoadFavoriteMovieCallback;
import com.triagung.moviecataloguefinalsub.LoadFavoriteTvShowCallback;
import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.asynctask.LoadMovieFavoriteAsync;
import com.triagung.moviecataloguefinalsub.asynctask.LoadTvShowFavoriteAsync;
import com.triagung.moviecataloguefinalsub.fragment.FavoriteFragment;
import com.triagung.moviecataloguefinalsub.fragment.MovieFragment;
import com.triagung.moviecataloguefinalsub.fragment.SettingFragment;
import com.triagung.moviecataloguefinalsub.fragment.TvShowFragment;
import com.triagung.moviecataloguefinalsub.model.Movie;
import com.triagung.moviecataloguefinalsub.model.TvShow;
import com.triagung.moviecataloguefinalsub.observer.DataMovieObserver;
import com.triagung.moviecataloguefinalsub.observer.DataTvShowObserver;

import java.util.ArrayList;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV_SHOW;
import static com.triagung.moviecataloguefinalsub.helper.MappingHelper.mapCursorToArrayListMovieFavorite;
import static com.triagung.moviecataloguefinalsub.helper.MappingHelper.mapCursorToArrayListTvShowFavorite;

public class MainActivity extends AppCompatActivity implements LoadFavoriteMovieCallback, LoadFavoriteTvShowCallback {
    public static ArrayList<Movie> listMovieFavorite;
    public static ArrayList<TvShow> listTvShowFavorite;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_movie:
                            fragment = new MovieFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                    .commit();
                            setupActionBarTitle(getResources().getString(R.string.movie_catalogue));
                            return true;

                        case R.id.nav_tv_show:
                            fragment = new TvShowFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                    .commit();
                            setupActionBarTitle(getResources().getString(R.string.tv_show_catalogue));
                            return true;

                        case R.id.nav_favorite:
                            fragment = new FavoriteFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                    .commit();
                            setupActionBarTitle(getResources().getString(R.string.favorite));
                            return true;

                        case R.id.nav_setting:
                            fragment = new SettingFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                    .commit();
                            setupActionBarTitle(getResources().getString(R.string.settings));
                            return true;
                    }

                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) navigationView.setSelectedItemId(R.id.nav_movie);

        HandlerThread handlerThreadMovieFavorite = new HandlerThread("DataMovieObserver");
        handlerThreadMovieFavorite.start();
        Handler handlerMovieFavorite = new Handler(handlerThreadMovieFavorite.getLooper());
        DataMovieObserver myMovieObserver = new DataMovieObserver(handlerMovieFavorite, this);
        getContentResolver().registerContentObserver(CONTENT_URI_MOVIE, true, myMovieObserver);

        HandlerThread handlerThreadTvShowFavorite = new HandlerThread("DataTvShowObserver");
        handlerThreadTvShowFavorite.start();
        Handler handlerTvShowFavorite = new Handler(handlerThreadTvShowFavorite.getLooper());
        DataTvShowObserver myTvShowObserver = new DataTvShowObserver(handlerTvShowFavorite, this);
        getContentResolver().registerContentObserver(CONTENT_URI_TV_SHOW, true, myTvShowObserver);

        new LoadMovieFavoriteAsync(this, this).execute();
        new LoadTvShowFavoriteAsync(this, this).execute();
    }

    private void setupActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void favoriteMoviePreExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listMovieFavorite = new ArrayList<>();
            }
        });
    }

    @Override
    public void favoriteMoviePostExecute(Cursor movieItems) {
        listMovieFavorite = mapCursorToArrayListMovieFavorite(movieItems);
    }

    @Override
    public void favoriteTvShowPreExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listTvShowFavorite = new ArrayList<>();
            }
        });
    }

    @Override
    public void favoriteTvShowPostExecute(Cursor tvShowItems) {
        listTvShowFavorite = mapCursorToArrayListTvShowFavorite(tvShowItems);
    }
}
