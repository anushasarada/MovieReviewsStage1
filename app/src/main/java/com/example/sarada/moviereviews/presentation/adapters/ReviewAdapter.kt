package com.example.sarada.moviereviews.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.presentation.adapters.ReviewAdapter.MyReviewViewHolder
import com.example.sarada.moviereviews.databinding.ReviewCardBinding
import com.example.sarada.moviereviews.models.datac.review.Review

class ReviewAdapter(
    private val mContext: Context
) : ListAdapter<Review, MyReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyReviewViewHolder {
        return MyReviewViewHolder.from(mContext, parent)
    }

    override fun onBindViewHolder(holder: MyReviewViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MyReviewViewHolder private constructor(
        private val mContext: Context,
        val binding: ReviewCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Review
        ) {
            binding.review = item
            binding.executePendingBindings()

            var avatarPath: String? = null
            if (item.authorDetails.avatarPath != null) {
                avatarPath =
                    if (item.authorDetails.avatarPath!!.length < 40)
                        "https://image.tmdb.org/t/p/w500${item.authorDetails.avatarPath}"
                    else item.authorDetails.avatarPath
            }
            Glide.with(mContext)
                .load(avatarPath)
                .apply(RequestOptions().placeholder(R.drawable.ic_outline_account_circle_24))
                .apply(RequestOptions().circleCrop())
                .into(binding.authorAvatar)

            var isTextViewExpanded = false
            binding.content.setOnClickListener {
                if (!isTextViewExpanded) {
                    binding.content.maxLines = 100
                    isTextViewExpanded = true
                } else {
                    binding.content.maxLines = 2
                    isTextViewExpanded = false
                }
            }
        }

        companion object {
            fun from(mContext: Context, parent: ViewGroup): MyReviewViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReviewCardBinding.inflate(layoutInflater, parent, false)
                return MyReviewViewHolder(mContext, binding)
            }
        }
    }
}

class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.author == newItem.author
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}