package com.example.sarada.moviereviews.bindingutils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sarada.moviereviews.models.datac.review.Review

@BindingAdapter("reviewAuthorText")
fun TextView.setReviewAuthorString(item: Review?) {
    item?.let {
        text = item.author
    }
}

@BindingAdapter("reviewContentText")
fun TextView.setReviewContentString(item: Review?) {
    item?.let {
        text = item.content
    }
}