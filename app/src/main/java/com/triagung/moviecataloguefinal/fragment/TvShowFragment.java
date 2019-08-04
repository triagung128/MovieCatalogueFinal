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
import com.triagung.moviecataloguefinal.adapter.TvShowAdapter;
import com.triagung.moviecataloguefinal.model.TvShow;
import com.triagung.moviecataloguefinal.viewmodel.TvShowViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    private TvShowAdapter tvShowAdapter;
    private ProgressBar progressBar;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                Toast.makeText(getActivity(), tvShow.getTitle(), Toast.LENGTH_SHORT).show();
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
