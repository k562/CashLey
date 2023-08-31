package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class ProfileUpdateData(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)