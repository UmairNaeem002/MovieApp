 package com.example.movieapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.movieapp.contract.Request
import com.example.movieapp.contract.Response
import com.example.movieapp.contract.Review
import com.example.movieapp.nertwork.IRequestContract
import com.example.movieapp.nertwork.NetworkClient
import com.example.movieapp.utils.Constant
import com.example.movieapp.utils.DataProvider
import com.example.movieapp.utils.showToast
import retrofit2.Call

 class AddorUpdateActivity : AppCompatActivity(), retrofit2.Callback<Response> {
     lateinit var userId:String
     private lateinit var sharedPreferences: SharedPreferences
     private lateinit var progressDialog: ProgressDialog
     private val retrofitClient = NetworkClient.getNetworkClient()
     private val requestContract = retrofitClient.create(IRequestContract::class.java)
     private var reason:Int = 0
     private lateinit var editedreview: Review

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addor_update)

        sharedPreferences = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(true)

        userId = sharedPreferences.getString(Constant.KEY_USER_ID,"").toString()
        reason = intent.getIntExtra(Constant.KEY_REASON, 0)
        renderUIForEdit()

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            val title = findViewById<EditText>(R.id.edTitle).text.toString().trim()
            val description = findViewById<EditText>(R.id.edDescription).text.toString().trim()
            if(title.isNotEmpty() && description.isNotEmpty()){
                var request = Request()
                if(reason==2){
                    val request = Request(action = Constant.UPDATE_REVIEW, userId = userId , reviewId = editedreview.reviewId, title = title ,description = description)
                }
                else{
                    val request = Request(action = Constant.ADD_REVIEW, userId = userId ,title = title, description = description)
                }
                progressDialog.show()
                val callResponse = requestContract.makeApiCall(request)
                callResponse.enqueue(this)
            }
            else{
                showToast("Please enter review details")
            }
        }
    }

     private fun renderUIForEdit(){
         if(reason==2){
             editedreview = DataProvider.review
             findViewById<EditText>(R.id.edTitle).setText(editedreview.title)
             findViewById<EditText>(R.id.edDescription).setText(editedreview.description)
             findViewById<Button>(R.id.btnSubmit).text = resources.getString(R.string.update)
         }
     }

     override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
         if(progressDialog.isShowing)
             progressDialog.dismiss()
         if(response.body()!=null){
             val serverResponse = response.body()
             if(serverResponse!!.status){
                 showToast(serverResponse.message)
                 Intent(this,HomeActivity::class.java).apply{
                     flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                     startActivity(this)
                 }
             }
             else{
                 showToast(serverResponse.message) }
         }
         else{
             showToast("Server is not responding. Please contact your system administrator.")
         }
     }

     override fun onFailure(call: Call<Response>, t: Throwable) {
         if(progressDialog.isShowing)
             progressDialog.dismiss()
         showToast("Server is not responding. Please contact your system administrator.")
     }
 }