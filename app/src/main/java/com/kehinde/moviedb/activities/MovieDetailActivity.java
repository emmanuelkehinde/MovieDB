package com.kehinde.moviedb.activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.data.Constants;
import com.kehinde.moviedb.data.Util;
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

        if (getIntent()!=null && getIntent().hasExtra(Constants.BUNDLE)) {
            bundle = getIntent().getBundleExtra(Constants.BUNDLE);
            //Set Title as Movie Title
            Movie movie=new Movie(bundle);
            getSupportActionBar().setTitle(movie.getTitle());
        }

        if (getSupportFragmentManager().findFragmentByTag(Constants.FRAG_TAG) ==  null) {
            movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailContainer, movieDetailFragment, Constants.FRAG_TAG)
                    .commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            if (Util.getRatingBy().equals(Constants.FAVOURITE)){
                startActivity(new Intent(this,MovieGridActivity.class));
                finish();
            }
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (Util.getRatingBy().equals(Constants.FAVOURITE)){
            startActivity(new Intent(this,MovieGridActivity.class));
            finish();
        }

        super.onBackPressed();
    }
}
