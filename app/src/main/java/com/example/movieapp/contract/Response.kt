package com.example.movieapp.contract

data class Response (
    var status:Boolean = false,
    var resonseCode:Int = -1,
    var message:String = "",
    var userId:String = "",
    var reviewId:String = "",
    var allReviews:MutableList<Review> = mutableListOf(),
    var myReviews:MutableList<Review> = mutableListOf()
        )