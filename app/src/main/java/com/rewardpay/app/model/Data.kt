package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("appUrl")
    val appUrl: String,
    @SerializedName("forceUpdate")
    val forceUpdate: Boolean,
    @SerializedName("socialEmail")
    val socialEmail: String,
    @SerializedName("socialImgUrl")
    val socialImgUrl: String,
    @SerializedName("socialName")
    val socialName: String
)