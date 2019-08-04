package com.triagung.moviecataloguefinal.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.triagung.moviecataloguefinal.R;
import com.triagung.moviecataloguefinal.fragment.FavoriteFragment;
import com.triagung.moviecataloguefinal.fragment.MovieFragment;
import com.triagung.moviecataloguefinal.fragment.TvShowFragment;

public class MainActivity extends AppCompatActivity {

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
    }

    private void setupActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
