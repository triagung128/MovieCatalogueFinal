package com.triagung.moviecataloguefinalsub.fragment;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.activity.DetailActivity;
import com.triagung.moviecataloguefinalsub.activity.SearchActivity;
import com.triagung.moviecataloguefinalsub.adapter.MovieAdapter;
import com.triagung.moviecataloguefinalsub.model.Movie;
import com.triagung.moviecataloguefinalsub.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Objects;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class MovieFragment extends Fragment {
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar_movie);

        showLoading(true);

        setupViewModel();

        showRecyclerView(view);
    }

    private void setupViewModel() {
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.setListMovie();
        movieViewModel.getListMovies().observe(this, getListMovie);
    }

    private final Observer<ArrayList<Movie>> getListMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movie> movies) {
            if (movies != null) {
                movieAdapter.setData(movies);
                showLoading(false);
            }
        }
    };

    private void showRecyclerView(View view) {
        movieAdapter = new MovieAdapter(getActivity());
        movieAdapter.notifyDataSetChanged();

        RecyclerView rvListMovie = view.findViewById(R.id.rv_list_movie);
        rvListMovie.setHasFixedSize(true);
        rvListMovie.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvListMovie.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent moveDetail = new Intent(getActivity(), DetailActivity.class);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(searchView.getContext(), SearchActivity.class)));
            searchView.setQueryHint(getResources().getString(R.string.search_movie));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent searchMovieIntent = new Intent(getActivity(), SearchActivity.class);
                    searchMovieIntent.putExtra(SearchActivity.EXTRA_QUERY, query);
                    searchMovieIntent.putExtra(SearchActivity.EXTRA_TYPE, "movie");
                    startActivity(searchMovieIntent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }
}
