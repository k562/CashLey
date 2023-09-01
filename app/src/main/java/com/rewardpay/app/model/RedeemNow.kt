package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class RedeemNow(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("currentCoinBalance")
        val currentCoinBalance: String,
        @SerializedName("currentRupeeBalance")
        val currentRupeeBalance:String,
        @SerializedName("status")
        val status: String
    )
}