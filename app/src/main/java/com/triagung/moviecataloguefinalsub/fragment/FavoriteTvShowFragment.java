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

import com.triagung.moviecataloguefinalsub.LoadFavoriteTvShowCallback;
import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.activity.DetailActivity;
import com.triagung.moviecataloguefinalsub.activity.MainActivity;
import com.triagung.moviecataloguefinalsub.adapter.FavoriteTvShowAdapter;
import com.triagung.moviecataloguefinalsub.asynctask.LoadTvShowFavoriteAsync;
import com.triagung.moviecataloguefinalsub.model.TvShow;

import java.util.ArrayList;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV_SHOW;

public class FavoriteTvShowFragment extends Fragment implements LoadFavoriteTvShowCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private ProgressBar progressBar;
    private TextView tvIsEmpty;
    private FavoriteTvShowAdapter favoriteTvShowAdapter;

    public FavoriteTvShowFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar_tv_show_favorite);
        tvIsEmpty = view.findViewById(R.id.tv_empty_tv_show_favorite);

        showRecyclerView(view);

        if (savedInstanceState == null) {
            new LoadTvShowFavoriteAsync(getActivity(), this).execute();
        } else {
            ArrayList<TvShow> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) favoriteTvShowAdapter.setListTvShowFavorite(list);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteTvShowAdapter.getListTvShowFavorite());
    }

    private void showRecyclerView(View view) {
        RecyclerView rvListTvShowFavorite = view.findViewById(R.id.rv_list_tv_show_favorite);
        rvListTvShowFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvListTvShowFavorite.setHasFixedSize(true);

        favoriteTvShowAdapter = new FavoriteTvShowAdapter(getActivity());
        rvListTvShowFavorite.setAdapter(favoriteTvShowAdapter);

        favoriteTvShowAdapter.setOnItemClickCallback(new FavoriteTvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow tvShow, int position) {
                Intent moveDetail = new Intent(getActivity(), DetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI_TV_SHOW + "/" + tvShow.getId());
                moveDetail.setData(uri);
                moveDetail.putExtra(DetailActivity.EXTRA_TYPE, "tv_show");
                moveDetail.putExtra(DetailActivity.EXTRA_ID, tvShow.getId());
                moveDetail.putExtra(DetailActivity.EXTRA_POSITION, position);
                startActivityForResult(moveDetail, DetailActivity.REQUEST_UPDATE);
            }
        });
    }

    @Override
    public void favoriteTvShowPreExecute() {
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
    public void favoriteTvShowPostExecute(Cursor tvShowItems) {
        progressBar.setVisibility(View.INVISIBLE);

        ArrayList<TvShow> list = MainActivity.listTvShowFavorite;
        if (list.size() > 0) {
            favoriteTvShowAdapter.setListTvShowFavorite(list);
        } else {
            favoriteTvShowAdapter.setListTvShowFavorite(new ArrayList<TvShow>());
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
                    favoriteTvShowAdapter.removeItem(position);
                    boolean isEmpty = data.getBooleanExtra(DetailActivity.EXTRA_IS_EMPTY, false);
                    if (isEmpty) tvIsEmpty.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
