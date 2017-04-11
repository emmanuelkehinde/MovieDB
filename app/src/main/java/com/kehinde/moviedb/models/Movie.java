package com.kehinde.moviedb.models;

import android.os.Bundle;

/**
 * Created by kehinde on 4/10/17.
 */

public class Movie {

    private String title;
    private String poster_url;
    private String synopsis;
    private int rating;
    private String release_date;

    public Movie() {

    }

    public Movie(String title, String poster_url, String synopsis, int rating, String release_date) {
        this.title = title;
        this.poster_url = poster_url;
        this.synopsis = synopsis;
        this.rating = rating;
        this.release_date = release_date;
    }

    public Movie(Bundle arguments){
        if (arguments!=null){

        }
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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

        return bundle;
    }
}
