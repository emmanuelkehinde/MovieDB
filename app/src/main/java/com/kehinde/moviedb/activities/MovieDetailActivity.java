package com.kehinde.moviedb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.data.Constants;
import com.kehinde.moviedb.fragments.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Movie Details");

        MovieDetailFragment movieDetailFragment=new MovieDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detailContainer,movieDetailFragment)
                .commit();

        Bundle bundle=getIntent().getBundleExtra(Constants.BUNDLE);
        movieDetailFragment.setArguments(bundle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
