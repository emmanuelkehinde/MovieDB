package com.kehinde.moviedb.API;


import com.google.gson.JsonObject;
import com.kehinde.moviedb.data.Constants;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kehinde on 4/10/17.
 */

public interface APIService {

    @GET("movie/popular?api_key="+ Constants.API_KEY)
    Call<JsonObject> getPopularMovies();

    @GET("movie/top_rated?api_key="+ Constants.API_KEY)
    Call<JsonObject> getTopRatedMovies();
}
