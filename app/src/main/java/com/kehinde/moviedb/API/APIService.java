package com.kehinde.moviedb.API;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kehinde on 4/10/17.
 */

public interface APIService {

    @GET("search/users?q=type:user+location:lagos+language:java")
    Call<JsonObject> getPopularMovies();

    @GET("search/users?q=type:user+location:lagos+language:java")
    Call<JsonObject> getTopRatedMovies();
}
