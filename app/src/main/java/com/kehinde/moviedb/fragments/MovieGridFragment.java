package com.kehinde.moviedb.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kehinde.moviedb.API.APIService;
import com.kehinde.moviedb.R;
import com.kehinde.moviedb.adapters.MovieAdapter;
import com.kehinde.moviedb.data.Constants;
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

    public MovieGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Getting a list of popular movies...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        movieGridRecyclerView = (RecyclerView) mView.findViewById(R.id.movieGridRecyclerView);

        if (movieList.isEmpty()) {
            getAllMovies(Util.getRatingBy());
        }
        return mView;
    }

    private void getAllMovies(String rating_by) {

        if (Util.isOnline(getActivity())) {
            progressDialog.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIService apiService = retrofit.create(APIService.class);

            Call<JsonObject> call;
            if (rating_by.equals(Constants.POPULAR)){
                call = apiService.getPopularMovies();
            }else{
                call = apiService.getTopRatedMovies();
            }

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                    Toast.makeText(getActivity(), "Received", Toast.LENGTH_SHORT).show();
                    setAdapter();
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
                    movieGridRecyclerView.setLayoutManager(gridLayoutManager);
                    movieGridRecyclerView.setAdapter(movieAdapter);

                    progressDialog.hide();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    Snackbar snackbar = Snackbar.make(movieGridRecyclerView, t.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE);
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

            Snackbar snackbar = Snackbar.make(movieGridRecyclerView, "Unable to connect, check your Internet connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Reload", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getAllMovies(Util.getRatingBy());
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.sort_by_popular) {
            sortByPopular();
        }else {
            sortByTopRated();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortByPopular() {
        if (Util.getRatingBy().equals(Constants.POPULAR)){
            Toast.makeText(getActivity(), "Already rated by Popular", Toast.LENGTH_SHORT).show();
        }else{
            getAllMovies(Constants.POPULAR);
        }
    }

    private void sortByTopRated() {
        if (Util.getRatingBy().equals(Constants.TOP_RATED)){
            Toast.makeText(getActivity(), "Already rated by Top-Rated", Toast.LENGTH_SHORT).show();
        }else{
            getAllMovies(Constants.TOP_RATED);
        }
    }

    @Override
    public void onMoviePosterClick(int clickedPosterIndex) {
        Toast.makeText(getActivity(), clickedPosterIndex, Toast.LENGTH_SHORT).show();
    }

}
