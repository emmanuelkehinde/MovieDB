package com.kehinde.moviedb.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kehinde.moviedb.R;
import com.kehinde.moviedb.models.Review;
import com.kehinde.moviedb.models.Trailer;

import java.util.ArrayList;


/**
 * Created by kehinde on 4/10/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MovieViewHolder>{

    private FragmentActivity mContext;
    private ArrayList<Review> reviewList;


    public ReviewAdapter(FragmentActivity mContext, ArrayList<Review> reviewList) {
        this.mContext=mContext;
        this.reviewList=reviewList;

    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_review_content;
        private TextView txt_review_by;


        MovieViewHolder(final View itemView) {
            super(itemView);

            txt_review_content= (TextView) itemView.findViewById(R.id.txt_review_content);
            txt_review_by= (TextView) itemView.findViewById(R.id.txt_review_by);

        }
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_layout,parent,false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Review review=reviewList.get(position);

        holder.txt_review_by.setText("Posted by "+review.getAuthor());
        holder.txt_review_content.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


}
