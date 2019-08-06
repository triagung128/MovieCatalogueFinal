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
import com.triagung.moviecataloguefinal.adapter.TvShowAdapter;
import com.triagung.moviecataloguefinal.model.Movie;
import com.triagung.moviecataloguefinal.model.TvShow;
import com.triagung.moviecataloguefinal.viewmodel.SearchMovieViewModel;
import com.triagung.moviecataloguefinal.viewmodel.SearchTvShowViewModel;

import java.util.ArrayList;

import static com.triagung.moviecataloguefinal.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;
import static com.triagung.moviecataloguefinal.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV_SHOW;

public class SearchActivity extends AppCompatActivity {
    public static final String EXTRA_QUERY = "extra_query";
    public static final String EXTRA_TYPE = "extra_type";

    private MovieAdapter favoriteMovieAdapter;
    private TvShowAdapter favoriteTvShowAdapter;
    private ProgressBar progressBar;

    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = findViewById(R.id.progress_bar_search);

        query = getIntent().getStringExtra(EXTRA_QUERY);

        showLoading(true);

        setupActionBar();

        String type = getIntent().getStringExtra(EXTRA_TYPE);

        if (type.equals("movie")) {
            setupSearchMovieViewModel();
            showSearchMovieRecyclerView();

        } else {
            setupSearchTvShowViewModel();
            showSearchTvShowRecyclerView();
        }
    }

    private void setupSearchMovieViewModel() {
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

    private void setupSearchTvShowViewModel() {
        SearchTvShowViewModel searchTvShowViewModel = ViewModelProviders.of(this).get(SearchTvShowViewModel.class);
        searchTvShowViewModel.setSearchTvShow(query);
        searchTvShowViewModel.getSearchTvShow().observe(this, getListTvShow);
    }

    private final Observer<ArrayList<TvShow>> getListTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvShow> tvShows) {
            if (tvShows != null) {
                favoriteTvShowAdapter.setData(tvShows);
                showLoading(false);
            }
        }
    };

    private void showSearchMovieRecyclerView() {
        favoriteMovieAdapter = new MovieAdapter(this);
        favoriteMovieAdapter.notifyDataSetChanged();

        RecyclerView rvListSearchMovie = findViewById(R.id.rv_list_search);
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

    private void showSearchTvShowRecyclerView() {
        favoriteTvShowAdapter = new TvShowAdapter(this);
        favoriteTvShowAdapter.notifyDataSetChanged();

        RecyclerView rvListSearchTvShow = findViewById(R.id.rv_list_search);
        rvListSearchTvShow.setHasFixedSize(true);
        rvListSearchTvShow.setLayoutManager(new GridLayoutManager(this, 2));
        rvListSearchTvShow.setAdapter(favoriteTvShowAdapter);

        favoriteTvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow tvShow) {
                Intent moveDetail = new Intent(getApplicationContext(), DetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI_TV_SHOW + "/" + tvShow.getId());
                moveDetail.setData(uri);
                moveDetail.putExtra(DetailActivity.EXTRA_TYPE, "tv show");
                moveDetail.putExtra(DetailActivity.EXTRA_ID, tvShow.getId());
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
