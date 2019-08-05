package com.triagung.moviecataloguefinal.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.triagung.moviecataloguefinal.R;
import com.triagung.moviecataloguefinal.adapter.MovieAdapter;
import com.triagung.moviecataloguefinal.model.Movie;
import com.triagung.moviecataloguefinal.viewmodel.SearchMovieViewModel;

import java.util.ArrayList;

import static com.triagung.moviecataloguefinal.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class SearchMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_SEARCH = "extra_movie_search";

    private MovieAdapter favoriteMovieAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        progressBar = findViewById(R.id.progress_bar_search_movie);

        showLoading(true);

        setupActionBar();

        setupViewModel();

        showRecyclerView();
    }

    private void setupViewModel() {
        String query = getIntent().getStringExtra(EXTRA_MOVIE_SEARCH);

        SearchMovieViewModel searchMovieViewModel = ViewModelProviders.of(this).get(SearchMovieViewModel.class);
        searchMovieViewModel.setSearchMovie(query);
        searchMovieViewModel.getSearchMovie().observe(this, getListMovie);
    }

    private final Observer<ArrayList<Movie>> getListMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movie> movies) {
            if (movies != null) {
                favoriteMovieAdapter.setData(movies);
                showLoading(false);
            }
        }
    };

    private void showRecyclerView() {
        favoriteMovieAdapter = new MovieAdapter(this);
        favoriteMovieAdapter.notifyDataSetChanged();

        RecyclerView rvListSearchMovie = findViewById(R.id.rv_list_search_movie);
        rvListSearchMovie.setHasFixedSize(true);
        rvListSearchMovie.setLayoutManager(new GridLayoutManager(this, 2));
        rvListSearchMovie.setAdapter(favoriteMovieAdapter);

        favoriteMovieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent moveDetail = new Intent(getApplicationContext(), DetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movie.getId());
                moveDetail.setData(uri);
                moveDetail.putExtra(DetailActivity.EXTRA_TYPE, "movie");
                moveDetail.putExtra(DetailActivity.EXTRA_ID, movie.getId());
                startActivity(moveDetail);
            }
        });
    }

    private void showLoading(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.search_result));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
