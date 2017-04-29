package com.kehinde.moviedb.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kehinde.moviedb.API.APIService;
import com.kehinde.moviedb.R;
import com.kehinde.moviedb.adapters.ReviewAdapter;
import com.kehinde.moviedb.adapters.TrailerAdapter;
import com.kehinde.moviedb.data.Constants;
import com.kehinde.moviedb.data.DatabaseHelper;
import com.kehinde.moviedb.data.Util;
import com.kehinde.moviedb.models.Movie;
import com.kehinde.moviedb.models.Review;
import com.kehinde.moviedb.models.Trailer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements TrailerAdapter.MovieTrailerClickListener{


    private Movie movie;
    private CheckBox btn_favorite;
    private ImageView det_img_movie_poster;
    private TextView det_movie_title;
    private TextView det_movie_rating;
    private TextView det_movie_date;
    private TextView det_movie_overview;
    private ProgressBar det_progress_bar;
    private ProgressBar trailerPlaceholderProgress;
    private ProgressBar reviewPlaceholderProgress;
    private CoordinatorLayout det_coord_layout;
    private String movieId;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private RecyclerView trailersRecyclerView;
    private RecyclerView reviewsRecyclerView;
    private DatabaseHelper databaseHelper;




    private Bundle b;
    private ArrayList<Review> reviewList=new ArrayList<>();
    private ArrayList<Trailer> trailerList=new ArrayList<>();
    private TextView txt_tap_movie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null) {
            b = savedInstanceState.getBundle(Constants.STATE_BUNDLE);
        }else {
            b = getArguments();
        }

        if (b!=null){
            movie=new Movie(b);
        }
        
        databaseHelper=new DatabaseHelper(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView=  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        btn_favorite= (CheckBox) mView.findViewById(R.id.btn_favorite);
        det_img_movie_poster= (ImageView) mView.findViewById(R.id.det_img_movie_poster);
        det_movie_title=(TextView)mView.findViewById(R.id.det_movie_title);
        det_movie_rating=(TextView)mView.findViewById(R.id.det_movie_rating);
        det_movie_date=(TextView)mView.findViewById(R.id.det_movie_date);
        det_movie_overview=(TextView)mView.findViewById(R.id.det_movie_overview);
        det_progress_bar= (ProgressBar) mView.findViewById(R.id.det_progress_bar);
        trailerPlaceholderProgress= (ProgressBar) mView.findViewById(R.id.trailerPlaceholderProgress);
        reviewPlaceholderProgress= (ProgressBar) mView.findViewById(R.id.reviewPlaceholderProgress);
        det_coord_layout= (CoordinatorLayout) mView.findViewById(R.id.det_coord_layout);
        trailersRecyclerView= (RecyclerView) mView.findViewById(R.id.trailersRecyclerView);
        reviewsRecyclerView= (RecyclerView) mView.findViewById(R.id.reviewsRecyclerView);

        txt_tap_movie=(TextView) getActivity().findViewById(R.id.txt_tap_movie);

        //Make Tap Movie Instruction not visible on screen rotation
        if (txt_tap_movie != null) {
            if (movie != null) txt_tap_movie.setVisibility(View.GONE);
        }


        Picasso.with(getActivity()).load(movie.getPoster_url())
                .into(det_img_movie_poster, new Callback() {
                    @Override
                    public void onSuccess() {
                        det_progress_bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        det_progress_bar.setVisibility(View.GONE);
                    }
                });

        det_movie_title.setText(movie.getTitle());
        det_movie_rating.setText(movie.getRating()+ "/10");
        det_movie_date.setText(movie.getRelease_date());
        det_movie_overview.setText(movie.getSynopsis());
        movieId=movie.getId();

        //Check if movie is in favourite
        if (databaseHelper.isLocal(movieId)) btn_favorite.setChecked(true);

        //Get movie trailers
        getMovieReviewsAndTrailers();

        //Click function for the favourite star button
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_favorite.isChecked()){
                    databaseHelper.insertData(movieId,movie.getTitle(),movie.getPoster_url(),
                            movie.getSynopsis(),movie.getRating(),movie.getRelease_date());

                }else{
                    databaseHelper.deleteData(databaseHelper.getMovieLocalID(movieId));
                }
            }
        });
        
        return mView;
    }

    private void getMovieReviewsAndTrailers() {

        if (Util.isOnline(getActivity())) {

            getMovieTrailers();
            getMovieReviews();

        }else{
            Snackbar snackbar = Snackbar.make(det_coord_layout, "Unable to retrieve Trailers and Reviews; check your Internet connection", Snackbar.LENGTH_LONG);
            snackbar.setAction("Reload", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMovieReviewsAndTrailers();
                }
            });
            snackbar.show();
        }

    }

    private void getMovieReviews() {

        reviewPlaceholderProgress.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<JsonObject> reviewCall=apiService.getMovieReviews(movieId);
        reviewCall.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObject=response.body();
                JsonArray jsonArray=jsonObject.getAsJsonArray("results");

                for (JsonElement jsonElement:jsonArray){
                    reviewList.add(new Review(
                            jsonElement.getAsJsonObject().get("author").getAsString(),
                            jsonElement.getAsJsonObject().get("content").getAsString()
                    ));
                }
                reviewPlaceholderProgress.setVisibility(View.GONE);

                reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                reviewsRecyclerView.setLayoutManager(linearLayoutManager);
                if (getActivity()!=null) {
                    reviewsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                }
                reviewsRecyclerView.setAdapter(reviewAdapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                reviewPlaceholderProgress.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(det_coord_layout, "Unable to retrieve Reviews", Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getMovieReviews();
                    }
                });

            }
        });

    }

    private void getMovieTrailers() {

        trailerPlaceholderProgress.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<JsonObject> trailerCall=apiService.getMovieTrailers(movieId);
        trailerCall.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObject=response.body();
                JsonArray jsonArray=jsonObject.getAsJsonArray("results");

                for (JsonElement jsonElement:jsonArray){
                    trailerList.add(new Trailer(
                            jsonElement.getAsJsonObject().get("key").getAsString()
                    ));
                }
                trailerPlaceholderProgress.setVisibility(View.GONE);

                setTrailerAdapter();
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                trailersRecyclerView.setLayoutManager(linearLayoutManager);
                trailersRecyclerView.setAdapter(trailerAdapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                trailerPlaceholderProgress.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(det_coord_layout, "Unable to retrieve Trailers", Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getMovieTrailers();
                    }
                });

            }
        });

    }

    private void setTrailerAdapter() {
        trailerAdapter = new TrailerAdapter(getActivity(), trailerList, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(Constants.STATE_BUNDLE,b);

    }

    @Override
    public void onMovieTrailerClick(int clickedTrailerIndex) {
        String url="https://www.youtube.com/watch?v="+trailerList.get(clickedTrailerIndex).getKey();

        Intent urlIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        urlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(Intent.createChooser(urlIntent, "View Trailer"));
    }
}
