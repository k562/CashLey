package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class InstallLinkData(
    @SerializedName("message")
    val message: String,
    @SerializedName("offerLink")
    val offerLink: String,
    @SerializedName("status")
    val status: Int
)