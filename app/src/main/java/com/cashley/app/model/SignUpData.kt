package com.cashley.app.model


import com.google.gson.annotations.SerializedName

data class SignUpData(
    @SerializedName("message")
    val message: String,
    @SerializedName("securityToken")
    val securityToken: String,
    @SerializedName("socialImgUrl")
    val socialImgUrl: Any,
    @SerializedName("status")
    val status: Int,
    @SerializedName("userId")
    val userId: Int
)