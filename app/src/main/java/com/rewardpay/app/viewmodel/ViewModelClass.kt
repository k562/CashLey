package com.rewardpay.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rewardpay.app.model.EditProfileData
import com.rewardpay.app.model.HistoryData
import com.rewardpay.app.model.HomeData
import com.rewardpay.app.model.InstallLinkData
import com.rewardpay.app.model.OfferDetailsData
import com.rewardpay.app.model.RedeemNow
import com.rewardpay.app.model.SignUpData
import com.rewardpay.app.model.appOpenData
import com.rewardpay.app.model.inviteData
import com.rewardpay.app.repo.Repository

class ViewModelClass(application: Application) : AndroidViewModel(application) {
    var repository: Repository? = null

    init {
        repository = Repository()
    }

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
        return repository!!.signUp(
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
    }

    fun appOpen(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<appOpenData> {
        return repository!!.appOpen(userId, securityToken, versionName, versionCode)
    }

    fun getHome(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<HomeData> {
        return repository!!.getHome(userId, securityToken, versionName, versionCode)
    }

    fun getOfferDetails(
        offerId: String?,
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<OfferDetailsData> {
        return repository!!.getOfferDetails(
            offerId,
            userId,
            securityToken,
            versionName,
            versionCode
        )
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
        return repository!!.getInstallLink(
            offerId,
            userId,
            securityToken,
            versionName,
            versionCode,
            mobile,
            upiId
        )
    }

    fun getInvite(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<inviteData> {
        return repository!!.getInvite(userId, securityToken, versionName, versionCode)
    }

    fun getHistory(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?
    ): MutableLiveData<HistoryData> {
        return repository!!.getHistory(userId, securityToken, versionName, versionCode)
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
        return repository!!.getRedeemAmount(
            userId,
            securityToken,
            versionName,
            versionCode,
            mobile,
            upiId,
            amount
        )
    }

    fun getEditProfile(
        userId: String?,
        securityToken: String?,
        versionName: String?,
        versionCode: String?,
        actionType: String?,
        mobile: String?,
        dateOfBirth: String?,
        gender: String?,
        occupation: String?
    ): MutableLiveData<EditProfileData> {
        return repository!!.getEditProfile(
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
    }
}