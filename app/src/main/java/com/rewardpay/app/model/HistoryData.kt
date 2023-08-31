package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class HistoryData(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("referHistory")
        val referHistory: ArrayList<ReferHistory>,
//        @SerializedName("totalReferAmount")
//        val totalReferAmount: Int,
        @SerializedName("currentBalance")
        val currentBalance: CurrentBalance,
        @SerializedName("offerHistory")
        val offerHistory:ArrayList<OfferHistory>
    ) {
        data class CurrentBalance(
            @SerializedName("currentBalance")
            val currentBalance: String
        )

        data class OfferHistory(
            @SerializedName("amount")
            val amount: String,
            @SerializedName("date")
            val date: String,
            @SerializedName("offerImageUrl")
            val offerImageUrl: String,
            @SerializedName("offerName")
            val offerName: String
        )

        data class ReferHistory(
            @SerializedName("amount")
            val amount: String,
            @SerializedName("date")
            val date: String,
            @SerializedName("referImageUrl")
            val referImageUrl: String,
            @SerializedName("referName")
            val referName: String
        )
    }
}