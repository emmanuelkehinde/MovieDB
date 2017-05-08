package com.kehinde.moviedb.activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.data.Constants;
import com.kehinde.moviedb.data.DatabaseHelper;
import com.kehinde.moviedb.data.Util;
import com.kehinde.moviedb.fragments.MovieDetailFragment;
import com.kehinde.moviedb.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private Bundle bundle;
    private MovieDetailFragment movieDetailFragment;
    private DatabaseHelper databaseHelper;
    private int itemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper=new DatabaseHelper(this);
        itemCount=databaseHelper.viewAllData().getCount();

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
                //Check if there is a change in the number of movies, then open a new activity or finish
                if (databaseHelper.viewAllData().getCount() != itemCount){
                    Intent intent=new Intent(this,MovieGridActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    finish();
                }
            }
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (Util.getRatingBy().equals(Constants.FAVOURITE)){
            //Check if there is a change in the number of movies, then open a new activity or finish
            if (databaseHelper.viewAllData().getCount() != itemCount){
                Intent intent=new Intent(this,MovieGridActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                finish();
            }
        }

        super.onBackPressed();
    }
}
