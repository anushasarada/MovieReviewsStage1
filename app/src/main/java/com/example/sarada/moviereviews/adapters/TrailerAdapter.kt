package com.example.sarada.moviereviews.adapters

import android.content.Context
import com.example.sarada.moviereviews.models.datac.Trailer
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.adapters.TrailerAdapter.TrailerViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.sarada.moviereviews.databinding.TrailerCardBinding

/**
 * Created by delaroy on 5/24/17.
 */
class TrailerAdapter(private val mContext: Context, private val trailerList: List<Trailer>) :
    RecyclerView.Adapter<TrailerViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): TrailerViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = TrailerCardBinding.inflate(layoutInflater, viewGroup, false)
        return TrailerViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: TrailerViewHolder, i: Int) {
        viewHolder.binding.title.text = trailerList[i].name
    }

    override fun getItemCount(): Int {
        return trailerList.size
    }

    inner class TrailerViewHolder(val binding: TrailerCardBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem = trailerList[pos]
                    val videoId = trailerList[pos].key
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=$videoId")
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("VIDEO_ID", videoId)
                    mContext.startActivity(intent)
                    Toast.makeText(
                        v.context,
                        "You clicked " + clickedDataItem.name,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}