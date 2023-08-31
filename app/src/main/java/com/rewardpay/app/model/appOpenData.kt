package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class appOpenData(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)