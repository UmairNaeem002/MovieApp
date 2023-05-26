package com.example.movieapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.adapter.AllReviewAdapter
import com.example.movieapp.contract.Review
import com.example.movieapp.utils.DataProvider

class ViewAllReviews : AppCompatActivity() {

    lateinit var adapter:AllReviewAdapter
    lateinit var dataSource:MutableList<Review>
    private lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_reviews)

        context = this
        dataSource = DataProvider.response.allReviews
        adapter = AllReviewAdapter(context,dataSource)
        findViewById<RecyclerView>(R.id.rvAllReviews).adapter = adapter

    }
}