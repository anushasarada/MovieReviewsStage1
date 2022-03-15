package com.example.sarada.moviereviews.adapters

import android.content.Context
import com.example.sarada.moviereviews.models.datac.Trailer
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.adapters.TrailerAdapter.MyViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.sarada.moviereviews.R
import android.widget.TextView
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast

/**
 * Created by delaroy on 5/24/17.
 */
class TrailerAdapter(private val mContext: Context, private val trailerList: List<Trailer>) :
    RecyclerView.Adapter<MyViewHolder>() {

    //private lateinit var binding: TrailerCardBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.trailer_card, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        viewHolder.title.text = trailerList[i].name
    }

    override fun getItemCount(): Int {
        return trailerList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        //var thumbnail: ImageView

        init {
            title = view.findViewById<View>(R.id.title) as TextView
            //thumbnail = view.findViewById<View>(R.id.mainActivity_image) as ImageView
            view.setOnClickListener { v ->
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