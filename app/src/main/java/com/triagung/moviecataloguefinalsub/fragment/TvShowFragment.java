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
import com.triagung.moviecataloguefinalsub.adapter.TvShowAdapter;
import com.triagung.moviecataloguefinalsub.model.TvShow;
import com.triagung.moviecataloguefinalsub.viewmodel.TvShowViewModel;

import java.util.ArrayList;
import java.util.Objects;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV_SHOW;

public class TvShowFragment extends Fragment {
    private TvShowAdapter tvShowAdapter;
    private ProgressBar progressBar;

    public TvShowFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar_tv_show);

        showLoading(true);

        setupViewModel();

        showRecyclerView(view);
    }

    private void setupViewModel() {
        TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.setListTvShow();
        tvShowViewModel.getListTvShows().observe(this, getListTvShow);
    }

    private final Observer<ArrayList<TvShow>> getListTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvShow> tvShows) {
            if (tvShows != null) {
                tvShowAdapter.setData(tvShows);
                showLoading(false);
            }
        }
    };

    private void showRecyclerView(View view) {
        tvShowAdapter = new TvShowAdapter(getActivity());
        tvShowAdapter.notifyDataSetChanged();

        RecyclerView rvListTvShow = view.findViewById(R.id.rv_list_tv_show);
        rvListTvShow.setHasFixedSize(true);
        rvListTvShow.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvListTvShow.setAdapter(tvShowAdapter);

        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow tvShow) {
                Intent moveDetail = new Intent(getActivity(), DetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI_TV_SHOW + "/" + tvShow.getId());
                moveDetail.setData(uri);
                moveDetail.putExtra(DetailActivity.EXTRA_TYPE, "tv_show");
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(searchView.getContext(), SearchActivity.class)));
            searchView.setQueryHint(getResources().getString(R.string.search_tv_show));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent searchTvShowIntent = new Intent(getActivity(), SearchActivity.class);
                    searchTvShowIntent.putExtra(SearchActivity.EXTRA_QUERY, query);
                    searchTvShowIntent.putExtra(SearchActivity.EXTRA_TYPE, "tv show");
                    startActivity(searchTvShowIntent);
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
