package com.example.sarada.moviereviews.adapters

import android.content.Context
import com.example.sarada.moviereviews.models.datac.MovieDetails
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.content.Intent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.activities.MovieDetailActivity
import com.example.sarada.moviereviews.databinding.MovieItemBinding

class MovieAdapter(
    private val mContext: Context
) :
    ListAdapter<MovieDetails, MovieAdapter.MovieViewHolder>(MoviesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(mContext, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MovieViewHolder private constructor(
        private val mContext: Context,
        val binding: MovieItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(
            item: MovieDetails
        ) {

        val poster = "https://image.tmdb.org/t/p/w500" + item.posterPath
            Glide.with(mContext)
                .load(poster)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .into(binding.moviePosterImage)
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val intent = Intent(mContext, MovieDetailActivity::class.java)
                    intent.putExtra("movies", item)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext.startActivity(intent)
                }
            }
        }

        companion object {
            fun from(mContext: Context, parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
                return MovieViewHolder(mContext, binding)
            }
        }
    }
}

class MoviesDiffCallback: DiffUtil.ItemCallback<MovieDetails>() {
    override fun areItemsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
        return oldItem == newItem
    }

}