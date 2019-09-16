package com.triagung.moviecataloguefinalsub.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.triagung.moviecataloguefinalsub.LoadFavoriteMovieCallback;
import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.activity.DetailActivity;
import com.triagung.moviecataloguefinalsub.activity.MainActivity;
import com.triagung.moviecataloguefinalsub.adapter.FavoriteMovieAdapter;
import com.triagung.moviecataloguefinalsub.asynctask.LoadMovieFavoriteAsync;
import com.triagung.moviecataloguefinalsub.model.Movie;

import java.util.ArrayList;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class FavoriteMovieFragment extends Fragment implements LoadFavoriteMovieCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private ProgressBar progressBar;
    private TextView tvIsEmpty;
    private FavoriteMovieAdapter favoriteMovieAdapter;

    public FavoriteMovieFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar_movie_favorite);
        tvIsEmpty = view.findViewById(R.id.tv_empty_movie_favorite);

        showRecyclerView(view);

        if (savedInstanceState == null) {
            new LoadMovieFavoriteAsync(getActivity(), this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) favoriteMovieAdapter.setListMovieFavorite(list);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteMovieAdapter.getListMovieFavorite());
    }

    private void showRecyclerView(View view) {
        RecyclerView rvListMovieFavorite = view.findViewById(R.id.rv_list_movie_favorite);
        rvListMovieFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvListMovieFavorite.setHasFixedSize(true);

        favoriteMovieAdapter = new FavoriteMovieAdapter(getActivity());
        rvListMovieFavorite.setAdapter(favoriteMovieAdapter);

        favoriteMovieAdapter.setOnItemClickCallback(new FavoriteMovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie, int position) {
                Intent moveDetail = new Intent(getActivity(), DetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movie.getId());
                moveDetail.setData(uri);
                moveDetail.putExtra(DetailActivity.EXTRA_TYPE, "movie");
                moveDetail.putExtra(DetailActivity.EXTRA_ID, movie.getId());
                moveDetail.putExtra(DetailActivity.EXTRA_POSITION, position);
                startActivityForResult(moveDetail, DetailActivity.REQUEST_UPDATE);
            }
        });
    }

    @Override
    public void favoriteMoviePreExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void favoriteMoviePostExecute(Cursor movieItems) {
        progressBar.setVisibility(View.INVISIBLE);

        ArrayList<Movie> list = MainActivity.listMovieFavorite;
        if (list.size() > 0) {
            favoriteMovieAdapter.setListMovieFavorite(list);
        } else {
            favoriteMovieAdapter.setListMovieFavorite(new ArrayList<Movie>());
            tvIsEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == DetailActivity.REQUEST_UPDATE) {
                if (resultCode == DetailActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0);
                    favoriteMovieAdapter.removeItem(position);
                    boolean isEmpty = data.getBooleanExtra(DetailActivity.EXTRA_IS_EMPTY, false);
                    if (isEmpty) tvIsEmpty.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
