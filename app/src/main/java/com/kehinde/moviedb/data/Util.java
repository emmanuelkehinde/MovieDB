package com.kehinde.moviedb.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kehinde on 4/10/17.
 */

public class Util {

    private static String rating_by=Constants.POPULAR;

    public static boolean isOnline(Context context){

        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static void setRatingBy(String rating_by){
        Util.rating_by =rating_by;
    }

    public static String getRatingBy(){
        return rating_by;
    }

    public static Retrofit getRetrofitInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
