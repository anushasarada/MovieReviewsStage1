package com.example.sarada.moviereviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.adapters.ReviewAdapter.MyReviewViewHolder
import com.example.sarada.moviereviews.databinding.ReviewCardBinding
import com.example.sarada.moviereviews.models.datac.Review

class ReviewAdapter: ListAdapter<Review, MyReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyReviewViewHolder {
        return MyReviewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyReviewViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MyReviewViewHolder private constructor(val binding: ReviewCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: Review
        ) {
            binding.authorName.text = item.author
            binding.content.text = item.content
        }

        companion object {
            fun from(parent: ViewGroup): MyReviewViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReviewCardBinding.inflate(layoutInflater, parent, false)
                return MyReviewViewHolder(binding)
            }
        }
    }
}

class ReviewDiffCallback: DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.author == newItem.author
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}