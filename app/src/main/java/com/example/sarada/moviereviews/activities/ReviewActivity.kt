package com.example.sarada.moviereviews.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.widget.Toast
import com.example.sarada.moviereviews.models.Review
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sarada.moviereviews.models.ReviewResponse
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.RetrofitQuery
import com.example.sarada.moviereviews.adapters.ReviewAdapter
import com.example.sarada.moviereviews.adapters.TrailerAdapter
import com.example.sarada.moviereviews.databinding.ActivityMovieDetailBinding
import com.example.sarada.moviereviews.databinding.ActivityReviewBinding
import com.example.sarada.moviereviews.viewmodels.MovieDetailActivityViewModel
import com.example.sarada.moviereviews.viewmodels.ReviewActivityViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding

    private val viewModel: ReviewActivityViewModel by lazy {
        ViewModelProvider(this).get(ReviewActivityViewModel::class.java)
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
                recyclerView2!!.adapter = newReviews?.let { ReviewAdapter(applicationContext,
                    it.results!!
                ) }
                recyclerView2!!.smoothScrollToPosition(0)
            })

            /*val apiService = retrofit!!.create(
                RetrofitQuery::class.java
            )
            val call = apiService.getMovieReviews(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN)
            call!!.enqueue(object : Callback<ReviewResponse?> {
                override fun onResponse(
                    call: Call<ReviewResponse?>?,
                    response: Response<ReviewResponse?>?
                ) {
                    val review = response?.body()?.results
                    recyclerView2!!.adapter = review?.let { ReviewAdapter(applicationContext, it) }
                    recyclerView2!!.smoothScrollToPosition(0)
                }

                override fun onFailure(call: Call<ReviewResponse?>, t: Throwable) {
                    Log.d("Error", t.message!!)
                    Toast.makeText(
                        this@ReviewActivity,
                        "Error fetching trailer data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })*/
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