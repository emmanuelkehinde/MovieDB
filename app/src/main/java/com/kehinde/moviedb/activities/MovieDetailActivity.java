package com.kehinde.moviedb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.data.Constants;
import com.kehinde.moviedb.fragments.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MovieDetailFragment movieDetailFragment=new MovieDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detailContainer,movieDetailFragment)
                .commit();

        Bundle bundle=getIntent().getBundleExtra(Constants.BUNDLE);
        movieDetailFragment.setArguments(bundle);

    }
}
