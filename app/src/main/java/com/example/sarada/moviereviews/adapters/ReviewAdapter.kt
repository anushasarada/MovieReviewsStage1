package com.example.sarada.moviereviews.adapters

import android.content.Context
import com.example.sarada.moviereviews.models.datac.Review
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.adapters.ReviewAdapter.MyReviewViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.sarada.moviereviews.R
import android.widget.TextView
import com.example.sarada.moviereviews.databinding.ReviewCardBinding

class ReviewAdapter(private val mContext: Context, private val reviewList: List<Review>) :
    RecyclerView.Adapter<MyReviewViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyReviewViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = ReviewCardBinding.inflate(layoutInflater, viewGroup, false)
        return MyReviewViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyReviewViewHolder, i: Int) {
        viewHolder.binding.authorName.text = reviewList[i].author
        viewHolder.binding.content.text = reviewList[i].content
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    inner class MyReviewViewHolder(val binding: ReviewCardBinding) : RecyclerView.ViewHolder(binding.root)
}