package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class inviteData(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("description")
        val description: String,
        @SerializedName("perRefer")
        val perRefer: String,
        @SerializedName("referCode")
        val referCode: String,
        @SerializedName("other")
        val other: String,
        @SerializedName("facebook")
        val facebook:String,
        @SerializedName("whatsapp")
        val whatsapp:String,
        @SerializedName("telegram")
        val telegram:String
    )
}