package com.kehinde.moviedb.API;


import com.google.gson.JsonObject;
import com.kehinde.moviedb.data.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kehinde on 4/10/17.
 */

public interface APIService {

    @GET("movie/popular?api_key="+ Constants.API_KEY)
    Call<JsonObject> getPopularMovies();

    @GET("movie/top_rated?api_key="+ Constants.API_KEY)
    Call<JsonObject> getTopRatedMovies();

    @GET("movie/{id}/videos?api_key="+ Constants.API_KEY)
    Call<JsonObject> getMovieTrailers(@Path("id") String id);

    @GET("movie/{id}/reviews?api_key="+ Constants.API_KEY)
    Call<JsonObject> getMovieReviews(@Path("id") String id);
}
