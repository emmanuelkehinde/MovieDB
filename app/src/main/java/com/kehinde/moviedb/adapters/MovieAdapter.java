package com.kehinde.moviedb.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by kehinde on 4/10/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final FragmentActivity mContext;
    private final ArrayList<Movie> movieList;
    private ImageView img_movie_poster;

    final private MoviePosterClickListener moviePosterClickListener;


    public interface MoviePosterClickListener{
        void onMoviePosterClick(int clickedPosterIndex);
    }


    public MovieAdapter(FragmentActivity mContext, ArrayList<Movie> movieList,MoviePosterClickListener moviePosterClickListener) {
        this.mContext=mContext;
        this.movieList=movieList;
        this.moviePosterClickListener=moviePosterClickListener;

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public TextView devUName;

        public MovieViewHolder(final View itemView) {
            super(itemView);

            img_movie_poster=(ImageView) mContext.findViewById(R.id.img_movie_poster);

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
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie=movieList.get(position);

        Picasso.with(mContext).load(movie.getPoster_url()).into(img_movie_poster);




//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                boolean isTwoPane=false;
//                if (mContext.findViewById(R.id.detailContainer)!=null){
//                    isTwoPane=true;
//                }
//
//                if (isTwoPane){
//                    LinearLayout linearLayout= (LinearLayout)mContext.findViewById(R.id.instruction);
//                    if(linearLayout!=null){
//                        linearLayout.setVisibility(View.GONE);
//                    }
//
//                    DevelopersDetailsFragment developersDetailsFragment=new DevelopersDetailsFragment();
//                    developersDetailsFragment.setArguments(developer.toBundle());
//
//                    mContext.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.detailContainer,developersDetailsFragment)
//                            .commit();
//                }else {
//                    Intent intent=new Intent(mContext, DevelopersDetailsActivity.class);
//                    intent.putExtra(Constants.BUNDLE,developer.toBundle());
//                    mContext.startActivity(intent);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


}
