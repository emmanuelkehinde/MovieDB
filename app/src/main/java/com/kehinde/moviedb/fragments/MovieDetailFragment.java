package com.kehinde.moviedb.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {


    private Movie movie;
    private ImageView det_img_movie_poster;
    private TextView det_movie_title;
    private TextView det_movie_rating;
    private TextView det_movie_date;
    private TextView det_movie_overview;
    private ProgressBar det_progress_bar;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b=getArguments();
        if (b!=null){
            movie=new Movie(b);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView=  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        det_img_movie_poster= (ImageView) mView.findViewById(R.id.det_img_movie_poster);
        det_movie_title=(TextView)mView.findViewById(R.id.det_movie_title);
        det_movie_rating=(TextView)mView.findViewById(R.id.det_movie_rating);
        det_movie_date=(TextView)mView.findViewById(R.id.det_movie_date);
        det_movie_overview=(TextView)mView.findViewById(R.id.det_movie_overview);
        det_progress_bar= (ProgressBar) mView.findViewById(R.id.det_progress_bar);



        Picasso.with(getActivity()).load(movie.getPoster_url())
                .into(det_img_movie_poster, new Callback() {
                    @Override
                    public void onSuccess() {
                        det_progress_bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        det_progress_bar.setVisibility(View.GONE);
                    }
                });

        det_movie_title.setText(movie.getTitle());
        det_movie_rating.setText(movie.getRating()+ "/10");
        det_movie_date.setText(movie.getRelease_date());
        det_movie_overview.setText(movie.getSynopsis());

        return mView;
    }

}
