package com.example.sarada.moviereviews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sarada.moviereviews.models.MovieDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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
        String poster = "https://image.tmdb.org/t/p/w500" + movieList.get(position).getPosterPath();
        Glide.with(mContext)
                .load(poster)
                .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mainActivity_image) ImageView movieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        MovieDetails ClickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, MovieDetailActivity.class);
                        intent.putExtra("movies", ClickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
