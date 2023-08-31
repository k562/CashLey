package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class EditProfileData(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("dateOfBirth")
        val dateOfBirth: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("occupation")
        val occupation: String
    )
}