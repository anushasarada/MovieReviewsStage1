package com.example.sarada.moviereviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sarada.moviereviews.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyReviewViewHolder> {

    private Context mContext;
    private List<Review> reviewList;

    public ReviewAdapter(Context mContext, List<Review> reviewList){
        this.mContext = mContext;
        this.reviewList = reviewList;

    }

    @Override
    public ReviewAdapter.MyReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_card, viewGroup, false);
        return new ReviewAdapter.MyReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ReviewAdapter.MyReviewViewHolder viewHolder, int i){
        viewHolder.author.setText(reviewList.get(i).getAuthor());
        viewHolder.content.setText(reviewList.get(i).getContent());

    }

    @Override
    public int getItemCount(){

        return reviewList.size();

    }

    public class MyReviewViewHolder extends RecyclerView.ViewHolder{
        public TextView author, content;

        public MyReviewViewHolder(View view){
            super(view);
            author = (TextView) view.findViewById(R.id.author_name);
            content = (TextView) view.findViewById(R.id.content);
        }
    }
}
