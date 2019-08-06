package com.triagung.moviecataloguefinal.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.triagung.moviecataloguefinal.BuildConfig;
import com.triagung.moviecataloguefinal.model.Movie;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieDetailViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private final MutableLiveData<Movie> detailMovie = new MutableLiveData<>();

    public void setDetailMovie(String idMovie) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/"+ idMovie +"?api_key="+ API_KEY +"&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    Movie movie = new Movie(responseObject, true);
                    detailMovie.postValue(movie);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", "Get data failed!");
            }
        });
    }

    public LiveData<Movie> getMovieDetail() {
        return detailMovie;
    }
}
