package com.example.sarada.moviereviews.adapters

import android.content.Context
import com.example.sarada.moviereviews.models.Review
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.adapters.ReviewAdapter.MyReviewViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.sarada.moviereviews.R
import android.widget.TextView

class ReviewAdapter(private val mContext: Context, private val reviewList: List<Review>) :
    RecyclerView.Adapter<MyReviewViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyReviewViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.review_card, viewGroup, false)
        return MyReviewViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyReviewViewHolder, i: Int) {
        viewHolder.author.text = reviewList[i].author
        viewHolder.content.text = reviewList[i].content
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    inner class MyReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var author: TextView
        var content: TextView

        init {
            author = view.findViewById<View>(R.id.author_name) as TextView
            content = view.findViewById<View>(R.id.content) as TextView
        }
    }
}