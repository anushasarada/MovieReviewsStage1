package com.example.sarada.moviereviews.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.example.sarada.moviereviews.models.datac.review.Review
import com.example.sarada.moviereviews.viewmodels.ReviewViewModel

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding

    private val viewModel: ReviewViewModel by lazy {
        ViewModelProvider(this).get(ReviewViewModel::class.java)
    }

    var movieId = 0
    val reviewAdapter = ReviewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_review)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        movieId = intent.extras!!.getInt("movieId")

        this@ReviewActivity.title = """
            Reviews of 
            ${intent.extras!!.getString("movieName")}
            """.trimIndent()

        initViews()
    }

    private fun initViews() {
        val reviewList: List<Review> = ArrayList()

        val layoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.reviewsRecyclerView.layoutManager = layoutManager1
        binding.reviewsRecyclerView.adapter = reviewAdapter
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

            viewModel.movieId.value = movieId
            viewModel.reviews.observe(this) { newReviews ->
                if (newReviews.results != null && newReviews.results!!.isNotEmpty()) {
                    reviewAdapter.submitList(newReviews.results)
                    binding.apply {
                        reviewsRecyclerView.smoothScrollToPosition(0)
                        reviewsRecyclerView.visibility = View.VISIBLE
                        noReviewsTextView.visibility = View.GONE
                    }
                }else{
                    binding.apply {
                        reviewsRecyclerView.visibility = View.GONE
                        noReviewsTextView.visibility = View.VISIBLE
                    }
                }
            }

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