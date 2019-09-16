package com.triagung.moviecataloguefinalsub.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.triagung.moviecataloguefinalsub.BuildConfig;
import com.triagung.moviecataloguefinalsub.model.TvShow;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TvShowDetailViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private final MutableLiveData<TvShow> detailTvShow = new MutableLiveData<>();

    public void setDetailTvShow(String idTvShow) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/tv/"+ idTvShow +"?api_key="+ API_KEY +"&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    TvShow tvShow = new TvShow(responseObject, true);
                    detailTvShow.postValue(tvShow);
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

    public LiveData<TvShow> getTvShowDetail() {
        return detailTvShow;
    }
}
