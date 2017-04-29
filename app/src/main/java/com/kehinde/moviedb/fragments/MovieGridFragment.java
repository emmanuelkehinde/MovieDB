package com.kehinde.moviedb.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kehinde.moviedb.API.APIService;
import com.kehinde.moviedb.R;
import com.kehinde.moviedb.activities.MovieDetailActivity;
import com.kehinde.moviedb.adapters.MovieAdapter;
import com.kehinde.moviedb.data.Constants;
import com.kehinde.moviedb.data.DatabaseHelper;
import com.kehinde.moviedb.data.Util;
import com.kehinde.moviedb.models.Movie;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieGridFragment extends Fragment implements MovieAdapter.MoviePosterClickListener{


    private RecyclerView movieGridRecyclerView;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private MovieAdapter movieAdapter;
    private CoordinatorLayout coord_layout;
    private DatabaseHelper databaseHelper;

    public MovieGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        setHasOptionsMenu(true);

        databaseHelper=new DatabaseHelper(getActivity());


        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Getting a list of popular movies...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        movieGridRecyclerView = (RecyclerView) mView.findViewById(R.id.movieGridRecyclerView);
        coord_layout= (CoordinatorLayout) mView.findViewById(R.id.coord_layout);


        if (movieList!=null) {
            if (Util.getRatingBy().equals(Constants.FAVOURITE)){
                sortByFavourite();
            }else {
                getAllMovies(Util.getRatingBy());
            }
        }

        return mView;
    }

    private void getAllMovies(final String rating_by) {

        if (Util.isOnline(getActivity())) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIService apiService = retrofit.create(APIService.class);

            Call<JsonObject> call;
            if (rating_by.equals(Constants.POPULAR)){
                progressDialog.setMessage("Getting a list of popular movies...");
                progressDialog.show();
                call = apiService.getPopularMovies();
            }else{
                progressDialog.setMessage("Getting a list of top-rated movies...");
                progressDialog.show();
                call = apiService.getTopRatedMovies();
            }

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    movieList.clear();
                    JsonObject jsonObject=response.body();
                    JsonArray jsonArray=jsonObject.getAsJsonArray("results");

                    String image_path="http://image.tmdb.org/t/p/w185/";

                    for (JsonElement jsonElement:jsonArray){
                        movieList.add(new Movie(
                                jsonElement.getAsJsonObject().get("id").getAsString(),
                                jsonElement.getAsJsonObject().get("original_title").getAsString(),
                                image_path + jsonElement.getAsJsonObject().get("poster_path").getAsString(),
                                jsonElement.getAsJsonObject().get("overview").getAsString(),
                                jsonElement.getAsJsonObject().get("vote_average").getAsString(),
                                jsonElement.getAsJsonObject().get("release_date").getAsString()));
                    }



                    setAdapter();

                    GridLayoutManager gridLayoutManager = null;

                    if (getActivity()!=null) {
                        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                        } else {
                            gridLayoutManager = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
                        }
                    }else{
                        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                    }

                    movieGridRecyclerView.setLayoutManager(gridLayoutManager);
                    movieGridRecyclerView.setAdapter(movieAdapter);


                    if (rating_by.equals(Constants.POPULAR)){
                        Util.setRatingBy(Constants.POPULAR);
                    }else{
                        Util.setRatingBy(Constants.TOP_RATED);
                    }

                    progressDialog.hide();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    Log.e("error",t.getLocalizedMessage());
                    Snackbar snackbar = Snackbar.make(coord_layout, "Error, please try again", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Reload", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllMovies(Util.getRatingBy());
                        }
                    });
                    snackbar.show();

                    progressDialog.hide();
                }
            });

        }else{

            Snackbar snackbar = Snackbar.make(coord_layout, "Unable to connect, check your Internet connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Reload", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getAllMovies(rating_by);
                }
            });
            snackbar.show();

        }
    }

    private void setAdapter() {
        movieAdapter = new MovieAdapter(getActivity(), movieList, this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main,menu);

        if (Util.getRatingBy().equals(Constants.POPULAR))
            menu.getItem(0).setChecked(true);
        else if (Util.getRatingBy().equals(Constants.TOP_RATED))
            menu.getItem(1).setChecked(true);
        else menu.getItem(2).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setChecked(true);

        if (item.getItemId()==R.id.sort_by_popular) {
            sortByPopular();
        }else if(item.getItemId()==R.id.sort_by_top_rated){
            sortByTopRated();
        }else{
            sortByFavourite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortByFavourite() {

        if (Util.getRatingBy().equals(Constants.FAVOURITE) && !movieList.isEmpty()){
            Toast.makeText(getActivity(), "Already sorted by Favourite", Toast.LENGTH_SHORT).show();
        }else{
            Cursor cursor=databaseHelper.viewAllData();

            if (cursor.getCount()>0) {

                movieList.clear();
                while (cursor.moveToNext()) {
                    movieList.add(new Movie(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6)
                    ));
                }
                Util.setRatingBy(Constants.FAVOURITE);

                setAdapter();

                GridLayoutManager gridLayoutManager = null;

                if (getActivity()!=null) {
                    if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                    } else {
                        gridLayoutManager = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
                    }
                }else{
                    gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                }

                movieGridRecyclerView.setLayoutManager(gridLayoutManager);
                movieGridRecyclerView.setAdapter(movieAdapter);
            }else{
                Toast.makeText(getActivity(), "You have no movie marked as favourite.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void sortByPopular() {
        if (Util.getRatingBy().equals(Constants.POPULAR) && !movieList.isEmpty()){
            Toast.makeText(getActivity(), "Already sorted by Most Popular", Toast.LENGTH_SHORT).show();
        }else{
            getAllMovies(Constants.POPULAR);
        }
    }

    private void sortByTopRated() {
        if (Util.getRatingBy().equals(Constants.TOP_RATED) && !movieList.isEmpty()){
            Toast.makeText(getActivity(), "Already sorted by Top-Rated", Toast.LENGTH_SHORT).show();
        }else{
            getAllMovies(Constants.TOP_RATED);
        }
    }

    @Override
    public void onMoviePosterClick(int clickedPosterIndex) {

        Movie movie=movieList.get(clickedPosterIndex);

        boolean isTwoPane=false;
        if (getActivity().findViewById(R.id.detailContainer)!=null){
            isTwoPane=true;
        }

        if (isTwoPane){
            LinearLayout linearLayout= (LinearLayout)getActivity().findViewById(R.id.instruction);
            if(linearLayout!=null){
                linearLayout.setVisibility(View.GONE);
            }

            MovieDetailFragment movieDetailFragment=new MovieDetailFragment();
            movieDetailFragment.setArguments(movie.toBundle());

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailContainer,movieDetailFragment)
                    .commit();
        }else {
            Intent intent=new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(Constants.BUNDLE,movie.toBundle());
            getActivity().startActivity(intent);
        }
    }

}
