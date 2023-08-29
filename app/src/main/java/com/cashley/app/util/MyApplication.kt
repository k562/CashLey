package com.cashley.app.util

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration

class MyApplication : Application() {

    private lateinit var appOpenManager: ExampleAppOpenManager

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        AppLovinSdk.getInstance(this).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            appOpenManager = ExampleAppOpenManager(applicationContext)
        })
    }

    inner class ExampleAppOpenManager(applicationContext: Context) : LifecycleObserver,
        MaxAdListener {
        private var appOpenAd: MaxAppOpenAd
        private var context: Context

        init {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)

            context = applicationContext

            appOpenAd = MaxAppOpenAd("be463edb439a3cec", applicationContext!!)
            appOpenAd.setListener(this)
            appOpenAd.loadAd()
            if (appOpenAd.isReady) {
                appOpenAd.showAd()
            }

        }

        private fun showAdIfReady() {
            if (appOpenAd == null || !AppLovinSdk.getInstance(context).isInitialized) return
            if (appOpenAd.isReady) {
                appOpenAd.showAd()
            } else {
                appOpenAd.loadAd()
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            showAdIfReady()
        }

        override fun onAdLoaded(ad: MaxAd) {
//            if (appOpenAd.isReady) {
//                appOpenAd.showAd()
//            }
        }

        override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
            appOpenAd.loadAd()
        }

        override fun onAdDisplayed(ad: MaxAd) {}
        override fun onAdClicked(ad: MaxAd) {}

        override fun onAdHidden(ad: MaxAd) {
            appOpenAd.loadAd()
        }

        override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
            appOpenAd.loadAd()
        }
    }

    companion object {
        lateinit var appContext: Context
    }
}