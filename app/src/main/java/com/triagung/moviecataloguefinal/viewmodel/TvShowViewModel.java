package com.triagung.moviecataloguefinal.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.triagung.moviecataloguefinal.BuildConfig;
import com.triagung.moviecataloguefinal.model.TvShow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TvShowViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private final MutableLiveData<ArrayList<TvShow>> listTvShows = new MutableLiveData<>();

    public void setTvShow() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key="+ API_KEY +"&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject tvShow = results.getJSONObject(i);
                        TvShow listTvShows = new TvShow(tvShow, false);
                        listItems.add(listTvShows);
                    }

                    listTvShows.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", "Get data failed!");
            }
        });
    }

    public LiveData<ArrayList<TvShow>> getTvShows() {
        return listTvShows;
    }
}
