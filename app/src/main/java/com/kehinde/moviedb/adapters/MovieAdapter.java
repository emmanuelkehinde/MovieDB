package com.kehinde.moviedb.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.models.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by kehinde on 4/10/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private FragmentActivity mContext;
    private ArrayList<Movie> movieList;

    final private MoviePosterClickListener moviePosterClickListener;


    public interface MoviePosterClickListener{
        void onMoviePosterClick(int clickedPosterIndex);
    }


    public MovieAdapter(FragmentActivity mContext, ArrayList<Movie> movieList,MoviePosterClickListener moviePosterClickListener) {
        this.mContext=mContext;
        this.movieList=movieList;
        this.moviePosterClickListener=moviePosterClickListener;

    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private ImageView img_movie_poster;
        private ProgressBar placeholderProgress;

        MovieViewHolder(final View itemView) {
            super(itemView);

            img_movie_poster=(ImageView) itemView.findViewById(R.id.img_movie_poster);
            placeholderProgress= (ProgressBar) itemView.findViewById(R.id.placeholderProgress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            moviePosterClickListener.onMoviePosterClick(position);
        }
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_layout,parent,false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Movie movie=movieList.get(position);

        Picasso.with(mContext).load(movie.getPoster_url()).into(holder.img_movie_poster, new Callback() {
            @Override
            public void onSuccess() {
                holder.placeholderProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.placeholderProgress.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


}
