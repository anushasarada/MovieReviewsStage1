package com.example.sarada.moviereviews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by sarada on 3/8/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{

    private Context mContext;
    private List<MovieDetails> movieList;

    public MovieAdapter(Context mContext, List<MovieDetails> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_activity,parent,false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {

        //holder.movieName.setText(movieList.get(position).getOriginalTitle());

        //holder.releaseDate.setText(movieList.get(position).getReleaseDate());

        //String vote = Double.toString(movieList.get(position).getVoteAverage());
        //holder.movieRating.setText(vote);

        Glide.with(mContext)
                .load(movieList.get(position).getPosterPath())
                .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        //private TextView movieName, releaseDate, movieRating;
        private ImageView movieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.mainActivity_image);
            //movieName = itemView.findViewById(R.id.movie_title);
            //movieRating = itemView.findViewById(R.id.movie_rating);
            //releaseDate = itemView.findViewById(R.id.movie_release);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        MovieDetails ClickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, MovieDetailActivity.class);
                        intent.putExtra("original_title", movieList.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path", movieList.get(pos).getPosterPath());
                        intent.putExtra("overview", movieList.get(pos).getOverview());
                        intent.putExtra("vote_average", Double.toString(movieList.get(pos).getVoteAverage()));
                        intent.putExtra("release_date", movieList.get(pos).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked"+ClickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
