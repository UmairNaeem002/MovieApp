package com.example.movieapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent.Callback
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.movieapp.contract.Request
import com.example.movieapp.contract.Response
import com.example.movieapp.nertwork.IRequestContract
import com.example.movieapp.nertwork.NetworkClient
import com.example.movieapp.utils.Constant
import com.example.movieapp.utils.showToast
import retrofit2.Call

class MainActivity : AppCompatActivity(), retrofit2.Callback<Response> {
    private lateinit var progressDialog:ProgressDialog
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IRequestContract::class.java)
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var userName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(true)
        sharedPreferences = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)

        checkIfUserAlreadyRegistered()

        var butt = findViewById<Button>(R.id.btnReg)
        butt.setOnClickListener {
            userName = findViewById<EditText>(R.id.edUserName).text.toString().trim().uppercase()
            if(userName.isNullOrEmpty()){
                showToast("Please enter your name.")
            }
            else{
                progressDialog.show()
                val request = Request(action = Constant.REGISTER_USER, userName = userName)
                val callResponse = requestContract.makeApiCall(request)
                callResponse.enqueue(this)
            }

        }
    }

    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        if(response.body()!=null){
            val serverResponse = response.body()
            if(serverResponse!!.status){
                saveUserToPref(serverResponse.userId,userName)
                Intent(this,HomeActivity::class.java).apply{
                    putExtra(Constant.KEY_USER_ID,serverResponse.userId)
                    putExtra(Constant.KEY_USER_NAME,userName)
                    startActivity(this)
                    finish()
                }
            }
            else{
                showToast(serverResponse.message)
                findViewById<EditText>(R.id.edUserName).setText("")
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
        findViewById<EditText>(R.id.edUserName).setText("")
    }

    private fun saveUserToPref(userId:String,userName:String){
        val editor = sharedPreferences.edit()
        editor.putString(Constant.KEY_USER_ID,userId)
        editor.putString(Constant.KEY_USER_NAME,userName)
        editor.commit()
    }

    private fun checkIfUserAlreadyRegistered(){
        val userId = sharedPreferences.getString(Constant.KEY_USER_ID,"invalid user id")
        val userName = sharedPreferences.getString(Constant.KEY_USER_NAME,"invalid user name")
        if(!userId.contentEquals("invalid user id") && !userName.contentEquals("invalid user name")){
            Intent(this,HomeActivity::class.java).apply {
                putExtra(Constant.KEY_USER_ID, userId)
                putExtra(Constant.KEY_USER_NAME, userName)
                startActivity(this)
                finish()
            }
        }
    }
}