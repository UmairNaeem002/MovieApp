package com.example.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.contract.Review

class AllReviewAdapter(var context:Context, var dataSource:MutableList<Review>): RecyclerView.Adapter<AllReviewAdapter.AllReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllReviewViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.it_all_review,parent,false)
        return AllReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: AllReviewViewHolder, position: Int) {
        val review = dataSource[position]
        holder.title.text = review.title
        holder.description.text = review.description
        holder.blogger.text = review.reviewerName
        holder.dateTime.text = review.dateTime
    }

    class AllReviewViewHolder(view:View):RecyclerView.ViewHolder(view){
        var title = view.findViewById<TextView>(R.id.title)
        var description = view.findViewById<TextView>(R.id.description)
        var blogger = view.findViewById<TextView>(R.id.reviewerName)
        var dateTime = view.findViewById<TextView>(R.id.dateTime)
    }
}