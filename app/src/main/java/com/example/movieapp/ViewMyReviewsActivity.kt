package com.example.movieapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.adapter.AllReviewAdapter
import com.example.movieapp.adapter.MyReviewAdapter
import com.example.movieapp.contract.Review
import com.example.movieapp.utils.Constant
import com.example.movieapp.utils.DataProvider
import com.example.movieapp.utils.showToast

class ViewMyReviewsActivity : AppCompatActivity() {

    lateinit var adapter: MyReviewAdapter
    lateinit var dataSource:MutableList<Review>
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_my_reviews)

        context = this
        activity = this
        dataSource = DataProvider.response.allReviews
        if(dataSource.size>0){
            adapter = MyReviewAdapter(activity,context,dataSource)
            findViewById<RecyclerView>(R.id.rvAllReviews).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.NoReviews).visibility = View.INVISIBLE
            findViewById<RecyclerView>(R.id.rvMyReviews).adapter = adapter
        }
        else{
            findViewById<RecyclerView>(R.id.NoReviews).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.rvAllReviews).visibility = View.INVISIBLE
        }
        findViewById<ImageView>(R.id.add).setOnClickListener {
            showToast("Add Clicked")
        }

        findViewById<Button>(R.id.add).setOnClickListener {
            Intent(this,AddorUpdateActivity::class.java).apply {
                putExtra(Constant.KEY_REASON, 1)
               startActivity(this)
            }
        }

    }
}