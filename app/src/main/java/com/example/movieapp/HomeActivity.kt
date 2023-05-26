package com.example.movieapp


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.movieapp.contract.Request
import com.example.movieapp.contract.Response
import com.example.movieapp.nertwork.IRequestContract
import com.example.movieapp.nertwork.NetworkClient
import com.example.movieapp.utils.Constant
import com.example.movieapp.utils.DataProvider
import com.example.movieapp.utils.showToast
import retrofit2.Call

class HomeActivity : AppCompatActivity(), View.OnClickListener, retrofit2.Callback<Response> {
    lateinit var userId:String
    lateinit var userName:String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog: ProgressDialog
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IRequestContract::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(true)

        title = "Movie App"
        userId = intent.getStringExtra(Constant.KEY_USER_ID).toString()
        userName = intent.getStringExtra(Constant.KEY_USER_NAME).toString()

        DataProvider.userId = userId
        DataProvider.userName = userName

        findViewById<EditText>(R.id.txtUserName).setText("Welcome $userName")
        findViewById<Button>(R.id.allReviews).setOnClickListener(this)
        findViewById<Button>(R.id.myReviews).setOnClickListener(this)
        findViewById<Button>(R.id.signOut).setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        progressDialog.show()
        val request = Request(action = Constant.GET_REVIEW, userId = userId)
        val callResponse = requestContract.makeApiCall(request)
        callResponse.enqueue(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.allReviews -> {
                if(DataProvider.response.allReviews.size>0){
                    Intent(this,ViewAllReviews::class.java).apply {
                        startActivity(this)
                    }
                }
                else{
                    showToast("No reviews available")
                }
            }
            R.id.myReviews -> {
                Intent(this,ViewMyReviewsActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.signOut -> {
                signOut()
            }

            R.id.bbook -> {
                startActivity(Intent(this,BookActivity::class.java))
            }
            
        }
    }

    private fun signOut(){
        val editor = sharedPreferences.edit()
        editor.clear().commit()

        Intent(this,MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        if(response.body()!=null){
            val serverResponse = response.body()
            if(serverResponse!!.status){
                DataProvider.response = serverResponse
            }
            else{
                showToast(serverResponse.message)
            }
        }
        else{
            showToast("Server is not responding. Please contact your system administrator.")
            findViewById<EditText>(R.id.edUserName).setText("")
        }
    }

    override fun onFailure(call: Call<Response>, t: Throwable) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        showToast("Server is not responding. Please contact your system administrator.")
    }
}