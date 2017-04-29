package com.kehinde.moviedb.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.models.Movie;
import com.kehinde.moviedb.models.Trailer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by kehinde on 4/10/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieViewHolder>{

    private FragmentActivity mContext;
    private ArrayList<Trailer> trailerList;

    final private MovieTrailerClickListener movieTrailerClickListener;


    public interface MovieTrailerClickListener{
        void onMovieTrailerClick(int clickedTrailerIndex);
    }


    public TrailerAdapter(FragmentActivity mContext, ArrayList<Trailer> trailerList, MovieTrailerClickListener movieTrailerClickListener) {
        this.mContext=mContext;
        this.trailerList=trailerList;
        this.movieTrailerClickListener=movieTrailerClickListener;

    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView txt_trailer;

        MovieViewHolder(final View itemView) {
            super(itemView);

            txt_trailer= (TextView) itemView.findViewById(R.id.txt_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            movieTrailerClickListener.onMovieTrailerClick(position);
        }
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_layout,parent,false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Trailer trailer=trailerList.get(position);
        int p=position+1;
        holder.txt_trailer.setText("Trailer " + String.valueOf(p));


    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


}
