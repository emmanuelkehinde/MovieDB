package com.kehinde.moviedb.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.kehinde.moviedb.data.Constants;

/**
 * Created by kehinde on 4/10/17.
 */

public class Movie implements Parcelable {

    private String id;
    private String title;
    private String poster_url;
    private String synopsis;
    private String rating;
    private String release_date;

    public Movie() {

    }

    public Movie(String id, String title, String poster_url, String synopsis, String rating, String release_date) {
        this.id=id;
        this.title = title;
        this.poster_url = poster_url;
        this.synopsis = synopsis;
        this.rating = rating;
        this.release_date = release_date;
    }

    public Movie(Bundle arguments){
        if (arguments!=null){
            this.id = arguments.getString(Constants.ID);
            this.title = arguments.getString(Constants.TITLE);
            this.poster_url = arguments.getString(Constants.POSTER_URL);
            this.synopsis = arguments.getString(Constants.SYNOPSIS);
            this.rating = arguments.getString(Constants.RATING);
            this.release_date = arguments.getString(Constants.RELEASE_DATE);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Bundle toBundle(){
        Bundle bundle=new Bundle();
        bundle.putString(Constants.ID,id);
        bundle.putString(Constants.TITLE,title);
        bundle.putString(Constants.POSTER_URL,poster_url);
        bundle.putString(Constants.SYNOPSIS,synopsis);
        bundle.putString(Constants.RATING,rating);
        bundle.putString(Constants.RELEASE_DATE,release_date);
        return bundle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(poster_url);
        parcel.writeString(synopsis);
        parcel.writeString(rating);
        parcel.writeString(release_date);
    }

    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster_url = in.readString();
        synopsis = in.readString();
        rating = in.readString();
        release_date = in.readString();
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
