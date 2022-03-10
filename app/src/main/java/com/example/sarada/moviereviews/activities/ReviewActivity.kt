package com.example.sarada.moviereviews.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.adapters.ReviewAdapter
import com.example.sarada.moviereviews.databinding.ActivityReviewBinding
import com.example.sarada.moviereviews.models.Review
import com.example.sarada.moviereviews.viewmodels.ReviewViewModel

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding

    private val viewModel: ReviewViewModel by lazy {
        ViewModelProvider(this).get(ReviewViewModel::class.java)
    }

    var recyclerView2: RecyclerView? = null
    var movie_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_review)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        recyclerView2 = findViewById(R.id.recycler_view2)
        movie_id = intent.extras!!.getInt("movieId")
        this@ReviewActivity.title = """
            Reviews of 
            ${intent.extras!!.getString("movieName")}
            """.trimIndent()
        Toast.makeText(
            this@ReviewActivity,
            intent.extras!!.getString("movieName"),
            Toast.LENGTH_SHORT
        ).show()
        initViews1()
    }

    private fun initViews1() {
        val reviewList: List<Review> = ArrayList()
        val reviewAdapter = ReviewAdapter(this, reviewList)
        val layoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView2!!.layoutManager = layoutManager1
        recyclerView2!!.adapter = reviewAdapter
        reviewAdapter.notifyDataSetChanged()
        loadJSON1()
    }

    private fun loadJSON1() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please obtain your API Key from themoviedb.org",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            viewModel.movieId.value = movie_id
            viewModel.reviews.observe(this, androidx.lifecycle.Observer { newReviews ->
                if (newReviews.results == null){ Toast.makeText(
                    this@ReviewActivity,
                    "There are no reviews for this movie.",
                    Toast.LENGTH_SHORT
                ).show()
                }else{
                    recyclerView2!!.adapter = newReviews?.let { ReviewAdapter(applicationContext,
                        it.results!!
                    ) }
                    recyclerView2!!.smoothScrollToPosition(0)
                }
            })

        } catch (e: Exception) {
            Log.d("Error", e.message!!)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.review_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_item -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}