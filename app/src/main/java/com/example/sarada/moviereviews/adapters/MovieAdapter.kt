package com.example.sarada.moviereviews.adapters

import android.content.Context
import com.example.sarada.moviereviews.models.datac.MovieDetails
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.adapters.MovieAdapter.MovieViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.activities.MovieDetailActivity

class MovieAdapter(private val mContext: Context, private val movieList: List<MovieDetails>) :
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_activity, parent, false)
        return MovieViewHolder(v)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val poster = "https://image.tmdb.org/t/p/w500" + movieList[position].posterPath
        Glide.with(mContext)
            .load(poster)
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
            .into(holder.movieImage)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieImage: ImageView = itemView.findViewById(R.id.mainActivity_image)

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem = movieList[pos]
                    val intent = Intent(mContext, MovieDetailActivity::class.java)
                    intent.putExtra("movies", clickedDataItem)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext.startActivity(intent)
                }
            }
        }
    }
}