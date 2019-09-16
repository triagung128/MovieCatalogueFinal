package com.triagung.moviecataloguefinalsub.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.helper.MappingHelper;
import com.triagung.moviecataloguefinalsub.model.Movie;
import com.triagung.moviecataloguefinalsub.model.TvShow;
import com.triagung.moviecataloguefinalsub.viewmodel.MovieDetailViewModel;
import com.triagung.moviecataloguefinalsub.viewmodel.TvShowDetailViewModel;
import com.triagung.moviecataloguefinalsub.widget.FavoriteWidget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.provider.BaseColumns._ID;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.TvShowColumns;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV_SHOW;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_IS_EMPTY = "extra_is_empty";

    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 201;

    private ImageView imgBackdrop;
    private ImageView imgPoster;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvRating;
    private TextView tvOverview;
    private ImageView imgLove;

    private View line1;
    private View line2;
    private TextView tvType;
    private TextView tvLabelOverview;

    private ProgressBar progressBar;

    private int id;
    private String type;
    private int position;

    private boolean isFavorite = false;

    private Movie getMovie;
    private TvShow getTvShow;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgBackdrop = findViewById(R.id.img_backdrop_detail);
        imgPoster = findViewById(R.id.img_poster_detail);
        tvTitle = findViewById(R.id.tv_title_detail);
        tvReleaseDate = findViewById(R.id.tv_release_date_detail);
        tvRating = findViewById(R.id.tv_rating_detail);
        tvOverview = findViewById(R.id.tv_text_overview_detail);
        imgLove = findViewById(R.id.img_love_detail);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        tvType = findViewById(R.id.tv_type);
        tvLabelOverview = findViewById(R.id.tv_overview_detail);
        progressBar = findViewById(R.id.progress_bar_detail);

        imgLove.setOnClickListener(this);

        id = getIntent().getIntExtra(EXTRA_ID, 0);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        showLoading(true);

        if (type.equals("movie")) {
            setupMovieDetailViewModel();
        } else {
            setupTvShowDetailViewModel();
        }

        checkFavorite();

        setupActionBar();
    }

    private void checkFavorite() {
        uri = getIntent().getData();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                isFavorite = true;
                cursor.close();
                setFavorite();
            }
        }
    }

    private void setFavorite() {
        if (isFavorite) {
            imgLove.setImageResource(R.drawable.ic_favorite_white_32dp);
        } else {
            imgLove.setImageResource(R.drawable.ic_favorite_border_white_32dp);
        }
    }

    private void setupMovieDetailViewModel() {
        MovieDetailViewModel movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.setDetailMovie(String.valueOf(id));
        movieDetailViewModel.getMovieDetail().observe(this, getMovieDetail);
    }

    private void setupTvShowDetailViewModel() {
        TvShowDetailViewModel tvShowDetailViewModel = ViewModelProviders.of(this).get(TvShowDetailViewModel.class);
        tvShowDetailViewModel.setDetailTvShow(String.valueOf(id));
        tvShowDetailViewModel.getTvShowDetail().observe(this, getTvShowDetail);
    }

    private final Observer<Movie> getMovieDetail = new Observer<Movie>() {
        @Override
        public void onChanged(@Nullable Movie movie) {
            if (movie != null) {
                getMovie = movie;
                showMovieDetail(movie);
                showLoading(false);
            }
        }
    };

    private final Observer<TvShow> getTvShowDetail = new Observer<TvShow>() {
        @Override
        public void onChanged(@Nullable TvShow tvShow) {
            if (tvShow != null) {
                getTvShow = tvShow;
                showTvShowDetail(tvShow);
                showLoading(false);
            }
        }
    };

    private void showMovieDetail(Movie movie) {
        if (movie.getBackdrop().equals("null")) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342/" + movie.getPoster())
                    .apply(new RequestOptions().transform(new BlurTransformation(20)))
                    .into(imgBackdrop);
        } else {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342/" + movie.getBackdrop())
                    .apply(new RequestOptions().transform(new BlurTransformation(20)))
                    .into(imgBackdrop);
        }

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342/" + movie.getPoster())
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(
                        10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(imgPoster);

        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(dateFormat(movie.getDate()));
        tvRating.setText(String.valueOf(movie.getRating()));
        tvOverview.setText(movie.getOverview());

        tvType.setText(getString(R.string.movie));
        tvType.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_movie_white_24dp), null, null);
    }

    private void showTvShowDetail(TvShow tvShow) {
        if (tvShow.getBackdrop().equals("null")) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342/" + tvShow.getPoster())
                    .apply(new RequestOptions().transform(new BlurTransformation(20)))
                    .into(imgBackdrop);
        } else {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342/" + tvShow.getBackdrop())
                    .apply(new RequestOptions().transform(new BlurTransformation(20)))
                    .into(imgBackdrop);
        }

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342/" + tvShow.getPoster())
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(
                        10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(imgPoster);

        tvTitle.setText(tvShow.getTitle());
        tvReleaseDate.setText(dateFormat(tvShow.getDate()));
        tvRating.setText(String.valueOf(tvShow.getRating()));
        tvOverview.setText(tvShow.getOverview());
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String dateFormat(String date) {
        String result;
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date oldDate = oldDateFormat.parse(date);
            SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            result = newDateFormat.format(oldDate);
        } catch (ParseException e) {
            result = "Error";
            e.printStackTrace();
        }

        return result;
    }

    private void showLoading(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            tvRating.setVisibility(View.INVISIBLE);
            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.INVISIBLE);
            tvType.setVisibility(View.INVISIBLE);
            tvLabelOverview.setVisibility(View.INVISIBLE);
            imgLove.setVisibility(View.INVISIBLE);

        } else {
            progressBar.setVisibility(View.GONE);
            tvRating.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            tvType.setVisibility(View.VISIBLE);
            tvLabelOverview.setVisibility(View.VISIBLE);
            imgLove.setVisibility(View.VISIBLE);
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(findViewById(R.id.detail_activity), message, Snackbar.LENGTH_SHORT).show();
    }

    private void autoWidgetMovieFavoriteUpdate() {
        Intent updateWidget = new Intent(this, FavoriteWidget.class);
        updateWidget.setAction(FavoriteWidget.UPDATE_WIDGET);
        sendBroadcast(updateWidget);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_love_detail) {
            if (isFavorite) {
                getContentResolver().delete(uri, null, null);

                boolean isEmpty = false;

                if (type.equals("movie")) {
                    Cursor cursor = getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
                    if (cursor != null) {
                        isEmpty = MappingHelper.mapCursorToArrayListMovieFavorite(cursor).isEmpty();
                        cursor.close();
                    }
                } else {
                    Cursor cursor = getContentResolver().query(CONTENT_URI_TV_SHOW, null, null, null, null);
                    if (cursor != null) {
                        isEmpty = MappingHelper.mapCursorToArrayListTvShowFavorite(cursor).isEmpty();
                        cursor.close();
                    }
                }

                isFavorite = false;
                setFavorite();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_POSITION, position);
                intent.putExtra(EXTRA_IS_EMPTY, isEmpty);
                setResult(RESULT_DELETE, intent);

                autoWidgetMovieFavoriteUpdate();
                showSnackbarMessage(getResources().getString(R.string.remove_from_favorite));
            } else {
                if (type.equals("movie")) {
                    ContentValues values = new ContentValues();
                    values.put(_ID, getMovie.getId());
                    values.put(MovieColumns.BACKDROP, getMovie.getBackdrop());
                    values.put(MovieColumns.POSTER, getMovie.getPoster());
                    values.put(MovieColumns.TITLE, getMovie.getTitle());
                    values.put(MovieColumns.DATE, getMovie.getDate());
                    values.put(MovieColumns.YEAR, getMovie.getYear());
                    values.put(MovieColumns.RATING, getMovie.getRating());
                    values.put(MovieColumns.OVERVIEW, getMovie.getOverview());

                    getContentResolver().insert(CONTENT_URI_MOVIE, values);
                    isFavorite = true;
                    setFavorite();
                    autoWidgetMovieFavoriteUpdate();
                    showSnackbarMessage(getResources().getString(R.string.add_to_favorites));
                } else {
                    ContentValues values = new ContentValues();
                    values.put(_ID, getTvShow.getId());
                    values.put(TvShowColumns.BACKDROP, getTvShow.getBackdrop());
                    values.put(TvShowColumns.POSTER, getTvShow.getPoster());
                    values.put(TvShowColumns.TITLE, getTvShow.getTitle());
                    values.put(TvShowColumns.DATE, getTvShow.getDate());
                    values.put(TvShowColumns.YEAR, getTvShow.getYear());
                    values.put(TvShowColumns.RATING, getTvShow.getRating());
                    values.put(TvShowColumns.OVERVIEW, getTvShow.getOverview());

                    getContentResolver().insert(CONTENT_URI_TV_SHOW, values);
                    isFavorite = true;
                    setFavorite();
                    showSnackbarMessage(getResources().getString(R.string.add_to_favorites));
                }
            }
        }
    }
}
