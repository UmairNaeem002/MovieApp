package com.example.movieapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.AddorUpdateActivity
import com.example.movieapp.HomeActivity
import com.example.movieapp.R
import com.example.movieapp.contract.Request
import com.example.movieapp.contract.Response
import com.example.movieapp.contract.Review
import com.example.movieapp.nertwork.IRequestContract
import com.example.movieapp.nertwork.NetworkClient
import com.example.movieapp.utils.Constant
import com.example.movieapp.utils.DataProvider
import com.example.movieapp.utils.showToast
import retrofit2.Call

class MyReviewAdapter(var activity: Activity ,var context: Context, var dataSource:MutableList<Review>): RecyclerView.Adapter<MyReviewAdapter.MyReviewViewHolder>(), retrofit2.Callback<Response>  {

    private var progressDialog:ProgressDialog = ProgressDialog(context)
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IRequestContract::class.java)
    private lateinit var delreview:Review
    private var delposition:Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.it_my_review,parent,false)
        return MyReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: MyReviewViewHolder, position: Int) {
        val review = dataSource[position]
        holder.title.text = review.title
        holder.description.text = review.description
        holder.blogger.text = review.reviewerName
        holder.dateTime.text = review.dateTime

        holder.btnEdit.setOnClickListener {
            Intent(context,AddorUpdateActivity::class.java).apply {
                DataProvider.review = review
                putExtra(Constant.KEY_REASON, 2)
                activity.startActivity(this)
            }
        }
        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context).setTitle("App Alert").setMessage("ARE YOU SURE?")
                .setPositiveButton("Yes") { dialog, which ->
                    progressDialog.setMessage("Please wait..")
                    progressDialog.setCancelable(false)
                    delreview = review
                    delposition = position
                    val request = Request(
                        action = Constant.DELETE_REVIEW,
                        userId = DataProvider.userId,
                        reviewId = review.reviewId
                    )
                    progressDialog.show()
                    val callResponse = requestContract.makeApiCall(request)
                    callResponse.enqueue(this)

                }
                .setNegativeButton("No") { dialog, which ->
                    dialog?.dismiss()
                }
                .create()
                .show()

            progressDialog.setMessage("Please wait..")
            progressDialog.setCancelable(false)
            delreview = review
            delposition = position
            val request = Request(action = Constant.DELETE_REVIEW, userId = DataProvider.userId , reviewId = review.reviewId)
            progressDialog.show()
            val callResponse = requestContract.makeApiCall(request)
            callResponse.enqueue(this)
        }
    }

    class MyReviewViewHolder(view: View): RecyclerView.ViewHolder(view){
        var title = view.findViewById<TextView>(R.id.title)
        var description = view.findViewById<TextView>(R.id.description)
        var blogger = view.findViewById<TextView>(R.id.reviewerName)
        var dateTime = view.findViewById<TextView>(R.id.dateTime)
        var btnEdit = view.findViewById<TextView>(R.id.edit)
        var btnDelete = view.findViewById<TextView>(R.id.delete)
    }

    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        if(response.body()!=null){
            val serverResponse = response.body()
            if(serverResponse!!.status){
                dataSource.remove(delreview)
                notifyItemRemoved(delposition)
                notifyItemRangeChanged(delposition,dataSource.size)
                context.showToast(serverResponse.message)
            }
            else{
                context.showToast(serverResponse.message) }
        }
        else{
            context.showToast("Server is not responding. Please contact your system administrator.")
        }
    }

    override fun onFailure(call: Call<Response>, t: Throwable) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        context.showToast("Server is not responding. Please contact your system administrator.")
    }
}