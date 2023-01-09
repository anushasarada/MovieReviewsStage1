package com.example.sarada.moviereviews.presentation.adapters

import android.content.Context
import com.example.sarada.moviereviews.models.datac.Trailer
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.presentation.adapters.TrailerAdapter.TrailerViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.sarada.moviereviews.databinding.TrailerCardBinding

/**
 * Created by delaroy on 5/24/17.
 */
class TrailerAdapter(private val mContext: Context): ListAdapter<Trailer, TrailerViewHolder>(TrailerDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): TrailerViewHolder {
        return TrailerViewHolder.from(mContext, parent)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class TrailerViewHolder(private val mContext: Context, val binding: TrailerCardBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            item: Trailer
        ) {
        binding.title.text = item.name
        binding.root.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val videoId = item.key
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=$videoId")
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("VIDEO_ID", videoId)
                    mContext.startActivity(intent)
                    Toast.makeText(
                        v.context,
                        "You clicked " + item.name,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        companion object {
            fun from(mContext: Context, parent: ViewGroup): TrailerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TrailerCardBinding.inflate(layoutInflater, parent, false)
                return TrailerViewHolder(mContext, binding)
            }
        }
    }
}

class TrailerDiffCallback: DiffUtil.ItemCallback<Trailer>() {
    override fun areItemsTheSame(oldItem: Trailer, newItem: Trailer): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: Trailer, newItem: Trailer): Boolean {
        return oldItem == newItem
    }

}