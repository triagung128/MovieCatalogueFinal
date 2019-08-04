package com.triagung.moviecataloguefinal.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.triagung.moviecataloguefinal.R;
import com.triagung.moviecataloguefinal.adapter.MovieAdapter;
import com.triagung.moviecataloguefinal.model.Movie;
import com.triagung.moviecataloguefinal.viewmodel.MovieViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                Toast.makeText(getActivity(), movie.getTitle(), Toast.LENGTH_SHORT).show();
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
}
