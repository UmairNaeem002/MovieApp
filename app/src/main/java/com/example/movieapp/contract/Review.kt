package com.example.movieapp.contract

import com.google.gson.annotations.SerializedName

data class Review (
    @SerializedName("reviewId") var reviewId:String="",
    @SerializedName("reviewerName") var reviewerName:String="",
    @SerializedName("title") var title:String="",
    @SerializedName("description") var description:String="",
    @SerializedName("dateTime") var dateTime:String=""
        ) {
}