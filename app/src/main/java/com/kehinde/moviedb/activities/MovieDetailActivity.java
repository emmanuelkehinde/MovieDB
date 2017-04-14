package com.kehinde.moviedb.activities;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.data.Constants;
import com.kehinde.moviedb.fragments.MovieDetailFragment;
import com.kehinde.moviedb.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private Bundle bundle;
    private MovieDetailFragment movieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Movie Details");

        bundle = getIntent().getBundleExtra(Constants.BUNDLE);

        if (savedInstanceState == null) {
            movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailContainer, movieDetailFragment, Constants.FRAG_TAG)
                    .commit();
        }else{

            movieDetailFragment= (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAG_TAG);
        }



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
