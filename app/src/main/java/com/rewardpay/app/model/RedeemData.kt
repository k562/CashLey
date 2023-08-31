package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class RedeemData(
    @SerializedName("data")
    val `data` : Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("currentBalance")
        val currentBalance: String,
        @SerializedName("status")
        val status: String
    )
}