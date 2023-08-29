package com.cashley.app.model


import com.google.gson.annotations.SerializedName

data class OfferDetailsData(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("description")
        val description: Description,
        @SerializedName("offerLink")
        val offerLink: OfferLink,
        @SerializedName("offerDeatils")
        val offerDeatils: OfferDeatils
    ) {
        data class Description(
            @SerializedName("description")
            val description: String
        )

        data class OfferLink(
            @SerializedName("offerLink")
            val offerLink: String
        )

        data class OfferDeatils(
            @SerializedName("offerAmount")
            val offerAmount: String,
            @SerializedName("offerCategory")
            val offerCategory: String,
            @SerializedName("offerId")
            val offerId: Int,
            @SerializedName("offerImage")
            val offerImage: String,
            @SerializedName("offerName")
            val offerName: String,
            @SerializedName("perRefer")
            val perRefer: String
        )
    }
}