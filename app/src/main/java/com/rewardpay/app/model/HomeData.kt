package com.rewardpay.app.model


import com.google.gson.annotations.SerializedName

data class HomeData(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("allOffers")
        val allOffers: ArrayList<AllOffer>,
        @SerializedName("highestPaying")
        val highestPaying: ArrayList<HighestPaying>,
        @SerializedName("slider")
        val slider: ArrayList<SliderList>,
        @SerializedName("currentBalance")
        val currentBalance:String
    ) {
        data class SliderList(
            @SerializedName("offerId")
            val offerId: Int,
            @SerializedName("offerImage")
            val offerImage: String
        )

        data class AllOffer(
            @SerializedName("perRefer")
            val perRefer: String,
            @SerializedName("offerAmount")
            val offerAmount: String,
            @SerializedName("offerCategory")
            val offerCategory: String,
            @SerializedName("offerId")
            val offerId: Int,
            @SerializedName("offerName")
            val offerName: String,
            @SerializedName("offerImage")
            val offerImage: String
        )

        data class HighestPaying(
            @SerializedName("perRefer")
            val perRefer: String,
            @SerializedName("offerAmount")
            val offerAmount: String,
            @SerializedName("offerCategory")
            val offerCategory: String,
            @SerializedName("offerId")
            val offerId: Int,
            @SerializedName("offerName")
            val offerName: String,
            @SerializedName("offerImage")
            val offerImage: String
        )
    }
}