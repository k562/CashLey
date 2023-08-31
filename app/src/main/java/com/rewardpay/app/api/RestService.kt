package com.rewardpay.app.api

import com.rewardpay.app.model.*
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface RestService {

    @POST("sign_up")
    fun signUp(
        @Query("deviceId") deviceId: String?,
        @Query("deviceType") deviceType: String?,
        @Query("deviceName") deviceName: String?,
        @Query("socialType") socialType: String,
        @Query("socialId") socialId: String?,
        @Query("socialToken") socialToken: String?,
        @Query("socialEmail") socialEmail: String?,
        @Query("socialName") socialName: String?,
        @Query("socialImgUrl") socialImgUrl: String?,
        @Query("advertisingId") advertisingId: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?,
        @Query("utmSource") utmSource: String?,
        @Query("utmMedium") utmMedium: String?,
        @Query("utmTerm") utmTerm: String?,
        @Query("utmContent") utmContent: String?,
        @Query("utmCampaign") utmCampaign: String?,
        @Query("referrerUrl") referrerUrl: String?
    ): Call<SignUpData>

    @POST("appOpen")
    fun appOpen(
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?
    ): Call<appOpenData>

    @POST("home")
    fun getHome(
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?
    ): Call<HomeData>

    @POST("offerDetails")
    fun getOfferDetails(
        @Query("offerId") offerId: String?,
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?
    ): Call<OfferDetailsData>

    @POST("installLink")
    fun getInstallLink(
        @Query("offerId") offerId: String?,
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?,
        @Query("mobile") mobile: String?,
        @Query("upiId") upiId: String?
    ): Call<InstallLinkData>

    @POST("invite")
    fun getInvite(
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?
    ): Call<inviteData>

    @POST("balance")
    fun getHistory(
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?
    ): Call<HistoryData>

    @POST("redeem")
    fun getRedeemAmount(
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?,
        @Query("mobile") mobile: String?,
        @Query("upiId") upiId: String?,
        @Query("amount") amount: String?
    ): Call<RedeemNow>

    @POST("updateProfile")
    fun getEditProfile(
        @Query("userId") userId: String?,
        @Query("securityToken") securityToken: String?,
        @Query("versionName") versionName: String?,
        @Query("versionCode") versionCode: String?,
        @Query("actionType") actionType:String?,
        @Query("mobile") mobile:String?,
        @Query("dateOfBirth") dateOfBirth:String?,
        @Query("gender") gender:String?,
        @Query("occupation") occupation:String?
    ):Call<EditProfileData>

}