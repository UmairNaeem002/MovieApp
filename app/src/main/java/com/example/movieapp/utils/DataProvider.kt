package com.example.movieapp.utils

import com.example.movieapp.contract.Response
import com.example.movieapp.contract.Review

object DataProvider {
   var response:Response = Response()
   var review:Review = Review()
   lateinit var userId:String
   lateinit var userName:String
}