package com.kehinde.moviedb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.models.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {


    private Movie movie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView=  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Bundle b=getArguments();
        if (b!=null){
            movie=new Movie(b);
        }

        return mView;
    }

}
