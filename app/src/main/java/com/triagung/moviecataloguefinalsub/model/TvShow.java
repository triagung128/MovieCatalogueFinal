package com.triagung.moviecataloguefinalsub.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.TvShowColumns;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.getColumnDouble;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.getColumnInt;
import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.getColumnString;

public class TvShow implements Parcelable {
    private int id;
    private String backdrop;
    private String poster;
    private String title;
    private String date;
    private String year;
    private double rating;
    private String overview;

    public TvShow(JSONObject object, boolean isDetail) {
        if (isDetail) {
            try {
                int id = object.getInt("id");
                String poster = object.getString("poster_path");
                String backdrop = object.getString("backdrop_path");
                String title = object.getString("name");
                String first_air_date = object.getString("first_air_date");
                String[] first_air_date_part = first_air_date.split("-");
                double vote_average = object.getDouble("vote_average");
                String overview = object.getString("overview");

                this.id = id;
                this.backdrop = backdrop;
                this.poster = poster;
                this.title = title;
                this.date = first_air_date;
                this.year = first_air_date_part[0];
                this.rating = vote_average;
                this.overview = overview;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                int id = object.getInt("id");
                String poster = object.getString("poster_path");
                String title = object.getString("name");
                String first_air_date = object.getString("first_air_date");
                String[] first_air_date_parts = first_air_date.split("-");
                double rating = object.getDouble("vote_average");

                this.id = id;
                this.poster = poster;
                this.title = title;
                this.year = first_air_date_parts[0];
                this.rating = rating;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public TvShow() {
    }

    public TvShow(int id, String backdrop, String poster, String title, String date, String year, double rating, String overview) {
        this.id = id;
        this.backdrop = backdrop;
        this.poster = poster;
        this.title = title;
        this.date = date;
        this.year = year;
        this.rating = rating;
        this.overview = overview;
    }

    public TvShow(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.backdrop = getColumnString(cursor, TvShowColumns.BACKDROP);
        this.poster = getColumnString(cursor, TvShowColumns.POSTER);
        this.title = getColumnString(cursor, TvShowColumns.TITLE);
        this.date = getColumnString(cursor, TvShowColumns.DATE);
        this.year = getColumnString(cursor, TvShowColumns.YEAR);
        this.rating = getColumnDouble(cursor, TvShowColumns.RATING);
        this.overview = getColumnString(cursor, TvShowColumns.OVERVIEW);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.backdrop);
        dest.writeString(this.poster);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.year);
        dest.writeDouble(this.rating);
        dest.writeString(this.overview);
    }

    protected TvShow(Parcel in) {
        this.id = in.readInt();
        this.backdrop = in.readString();
        this.poster = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.year = in.readString();
        this.rating = in.readDouble();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<TvShow> CREATOR = new Parcelable.Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel source) {
            return new TvShow(source);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}
