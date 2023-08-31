package com.rewardpay.app.repo

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.rewardpay.app.api.RestClient
import com.rewardpay.app.model.EditProfileData
import com.rewardpay.app.model.HistoryData
import com.rewardpay.app.model.HomeData
import com.rewardpay.app.model.InstallLinkData
import com.rewardpay.app.model.OfferDetailsData
import com.rewardpay.app.model.RedeemNow
import com.rewardpay.app.model.SignUpData
import com.rewardpay.app.model.appOpenData
import com.rewardpay.app.model.inviteData
import com.rewardpay.app.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    var signUpDataList = MutableLiveData<SignUpData>()
    var appOpenDataList = MutableLiveData<appOpenData>()
    var homeDataList = MutableLiveData<HomeData>()
    var offerDetailsList = MutableLiveData<OfferDetailsData>()
    var installLinkList = MutableLiveData<InstallLinkData>()
    var inviteDataList = MutableLiveData<inviteData>()
    var historyList = MutableLiveData<HistoryData>()
    var redeemList = MutableLiveData<RedeemNow>()
    var editProfileList = MutableLiveData<EditProfileData>()

    fun signUp(
        deviceId: String?,
        deviceType: String?,
        deviceName: String?,
        socialType: String,
        socialId: String?,
        socialToken: String?,
        socialEmail: String?,
        socialName: String?,
        socialImgUrl: String?,
        advertisingId: String?,
        versionName: String?,
        versionCode: String?,
        utmSource: String?,
        utmMedium: String?,
        utmTerm: String?,
        utmContent: String?,
        utmCampaign: String?,
        referrerUrl: String?
    ): MutableLiveData<SignUpData> {
        val call: Call<SignUpData> = RestClient().getApi().signUp(
            deviceId,
            deviceType,
            deviceName,
            socialType,
            socialId,
            socialToken,
            socialEmail,
            socialName,
            socialImgUrl,
            advertisingId,
            versionName,
            versionCode,
            utmSource,
            utmMedium,
            utmTerm,
            utmContent,
            utmCampaign,
            referrerUrl
        )

        call.enqueue(object : Callback<SignUpData> {
            override fun onResponse(
                call: Call<SignUpData>,
                response: Response<SignUpData>
            ) {

                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        signUpDataList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SignUpData>, t: Throwable) {
                //  errorMessage.postValue(t.message)
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })
        return signUpDataList
    }

    fun appOpen(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<appOpenData> {
        val call: Call<appOpenData> =
            RestClient().getApi().appOpen(userId, securityToken, versionName, versionCode)
        call.enqueue(object : Callback<appOpenData> {
            override fun onResponse(call: Call<appOpenData>, response: Response<appOpenData>) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        appOpenDataList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<appOpenData>, t: Throwable) {
                //  errorMessage.postValue(t.message)
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })

        return appOpenDataList

    }

    fun getHome(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<HomeData> {
        val call: Call<HomeData> =
            RestClient().getApi().getHome(userId, securityToken, versionName, versionCode)
        call.enqueue(object : Callback<HomeData> {
            override fun onResponse(call: Call<HomeData>, response: Response<HomeData>) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        homeDataList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<HomeData>, t: Throwable) {
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })
        return homeDataList
    }

    fun getOfferDetails(
        offerId: String?,
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<OfferDetailsData> {
        val call: Call<OfferDetailsData> = RestClient().getApi()
            .getOfferDetails(offerId, userId, securityToken, versionName, versionCode)
        call.enqueue(object : Callback<OfferDetailsData> {
            override fun onResponse(
                call: Call<OfferDetailsData>,
                response: Response<OfferDetailsData>
            ) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        offerDetailsList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<OfferDetailsData>, t: Throwable) {
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })
        return offerDetailsList
    }

    fun getInstallLink(
        offerId: String?,
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?,
        mobile: String?,
        upiId: String?
    ): MutableLiveData<InstallLinkData> {
        val call: Call<InstallLinkData> = RestClient().getApi()
            .getInstallLink(offerId, userId, securityToken, versionName, versionCode, mobile, upiId)
        call.enqueue(object : Callback<InstallLinkData> {
            override fun onResponse(
                call: Call<InstallLinkData>,
                response: Response<InstallLinkData>
            ) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        installLinkList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<InstallLinkData>, t: Throwable) {
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })

        return installLinkList
    }

    fun getInvite(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<inviteData> {
        val call: Call<inviteData> =
            RestClient().getApi().getInvite(userId, securityToken, versionName, versionCode)
        call.enqueue(object : Callback<inviteData> {
            override fun onResponse(call: Call<inviteData>, response: Response<inviteData>) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        inviteDataList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<inviteData>, t: Throwable) {
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })
        return inviteDataList
    }

    fun getHistory(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<HistoryData> {
        val call: Call<HistoryData> =
            RestClient().getApi().getHistory(userId, securityToken, versionName, versionCode)
        call.enqueue(object : Callback<HistoryData> {
            override fun onResponse(call: Call<HistoryData>, response: Response<HistoryData>) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        historyList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<HistoryData>, t: Throwable) {
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })
        return historyList
    }

    fun getRedeemAmount(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?,
        mobile: String?,
        upiId: String?,
        amount: String?
    ): MutableLiveData<RedeemNow> {
        val call: Call<RedeemNow> = RestClient().getApi()
            .getRedeemAmount(userId, securityToken, versionName, versionCode, mobile, upiId, amount)
        call.enqueue(object : Callback<RedeemNow> {
            override fun onResponse(call: Call<RedeemNow>, response: Response<RedeemNow>) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        redeemList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RedeemNow>, t: Throwable) {
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })

        return redeemList
    }

    fun getEditProfile(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?,
        actionType:String?,
        mobile: String?,
        dateOfBirth: String?,
        gender: String?,
        occupation: String?
    ): MutableLiveData<EditProfileData> {
        val call: Call<EditProfileData> = RestClient().getApi().getEditProfile(
            userId,
            securityToken,
            versionName,
            versionCode,
            actionType,
            mobile,
            dateOfBirth,
            gender,
            occupation
        )
        call.enqueue(object : Callback<EditProfileData> {
            override fun onResponse(
                call: Call<EditProfileData>,
                response: Response<EditProfileData>
            ) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        editProfileList.postValue(response.body())
                    } else {
                        Toast.makeText(
                            MyApplication.appContext,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        MyApplication.appContext,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<EditProfileData>, t: Throwable) {
                Log.d("error", t.message.toString())
                Toast.makeText(MyApplication.appContext, t.message, Toast.LENGTH_SHORT).show()
            }

        })

        return editProfileList

    }

}