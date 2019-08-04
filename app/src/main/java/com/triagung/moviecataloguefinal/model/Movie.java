package com.triagung.moviecataloguefinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Movie implements Parcelable {
    private int id;
    private String backdrop;
    private String poster;
    private String title;
    private String date;
    private String year;
    private double rating;
    private String overview;

    public Movie(JSONObject object, boolean isDetail) {
        if (isDetail) {
            try {
                int id = object.getInt("id");
                String backdrop = object.getString("backdrop_path");
                String poster = object.getString("poster_path");
                String title = object.getString("title");
                String release_date = object.getString("release_date");
                String[] release_date_path = release_date.split("-");
                double vote_average = object.getDouble("vote_average");
                String overview = object.getString("overview");

                this.id = id;
                this.backdrop = "https://image.tmdb.org/t/p/w342/" + backdrop;
                this.poster = "https://image.tmdb.org/t/p/w342/" + poster;
                this.title = title;
                this.date = release_date;
                this.year = release_date_path[0];
                this.rating = vote_average;
                this.overview = overview;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                int id = object.getInt("id");
                String poster = object.getString("poster_path");
                String title = object.getString("title");
                String release_date = object.getString("release_date");
                String[] part_release_date = release_date.split("-");
                double vote_average = object.getDouble("vote_average");

                this.id = id;
                this.poster = "https://image.tmdb.org/t/p/w342/" + poster;
                this.title = title;
                this.year = part_release_date[0];
                this.rating = vote_average;

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

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.backdrop = in.readString();
        this.poster = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.year = in.readString();
        this.rating = in.readDouble();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
