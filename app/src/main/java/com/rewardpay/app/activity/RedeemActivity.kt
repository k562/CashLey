package com.rewardpay.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.rewardpay.app.BuildConfig
import com.rewardpay.app.api.RestClient
import com.rewardpay.app.databinding.ActivityRedeemBinding
import com.rewardpay.app.model.RedeemNow
import com.rewardpay.app.util.MyApplication
import com.rewardpay.app.util.MyPreference
import com.rewardpay.app.util.NetworkConfig
import com.rewardpay.app.viewmodel.ViewModelClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class RedeemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedeemBinding
    private var totalCoins: String? = "00"
    private var totalAmount: String? = "00"
    private lateinit var viewModel: ViewModelClass
    private var rewardedAd: MaxRewardedAd?=null
    private var retryAttempt = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedeemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //app lonin
        AppLovinSdk.getInstance( this).setMediationProvider( "max" )
        AppLovinSdk.getInstance( this ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            createAd()
        })

        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]

        if (intent != null) {
            totalCoins = intent.getStringExtra("Total_Coins")
            totalAmount=intent.getStringExtra("Total_Amount")
        }

        binding.totalCoins.text = totalCoins
        binding.totalAmount.text=totalAmount

        //redeem now
        binding.redeemNowButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            checkValidation()
        }

        //back button
        binding.toolBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun checkValidation() {
        if (binding.etMobileNumber.text.isEmpty() || binding.etMobileNumber.text.length > 10 || binding.etMobileNumber.text.length < 10) {
            binding.etMobileNumber.error = "Valid Mobile Number Required"
            binding.etMobileNumber.requestFocus()
            binding.progressBar.visibility = View.GONE
        } else if (isUpiIdValid(binding.upiId.text.toString()) == false) {
            binding.upiId.error = "Valid UPI-Id Required"
            binding.upiId.requestFocus()
            binding.progressBar.visibility = View.GONE
        } else if (binding.amount.text.toString().isEmpty() || binding.amount.text.toString()
                .toInt() <= 0
        ) {
            binding.amount.error = "Amount Required"
            binding.amount.requestFocus()
            binding.progressBar.visibility = View.GONE
        } else {
            if (NetworkConfig().networkIsConnected(this)) {
                if(rewardedAd!=null) {
                    if (rewardedAd!!.isReady()) {
                        rewardedAd!!.showAd()
                    }
                }
                else
                {
                    apiHit()
                }
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isUpiIdValid(upiId: String): Boolean {
        // Define a regular expression pattern for UPI IDs
        val regexPattern = "^[\\w.-]+@[\\w.-]+\$"

        // Validate the input against the pattern
        return upiId.matches(Regex(regexPattern))
    }

    fun apiHit() {
//        viewModel.getRedeemAmount(
//            MyPreference.getUserId(this).toString(),
//            MyPreference.getSecurityToken(this),
//            BuildConfig.VERSION_NAME,
//            BuildConfig.VERSION_CODE.toString(),
//            binding.etMobileNumber.text.toString(),
//            binding.upiId.text.toString(),
//            binding.amount.text.toString()
//        ).observe(this,
//            Observer {
//                it
//                binding.progressBar.visibility = View.GONE
//                binding.totalCoins.text = it.data.currentCoinBalance
//                binding.totalAmount.text=it.data.currentRupeeBalance
//                binding.etMobileNumber.text.clear()
//                binding.upiId.text.clear()
//                binding.amount.text.clear()
//                Toast.makeText(this, it.data.status, Toast.LENGTH_SHORT).show()
//            })

        //invalid user
        val call: Call<RedeemNow> = RestClient().getApi()
            .getRedeemAmount(MyPreference.getUserId(this).toString(),
                MyPreference.getSecurityToken(this),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString(),
                binding.etMobileNumber.text.toString(),
                binding.upiId.text.toString(),
                binding.amount.text.toString())
        call.enqueue(object : Callback<RedeemNow> {
            override fun onResponse(call: Call<RedeemNow>, response: Response<RedeemNow>) {
                if (response.body() != null) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        binding.progressBar.visibility = View.GONE
                        binding.totalCoins.text = response.body()?.data?.currentCoinBalance
                        binding.totalAmount.text=response.body()?.data?.currentRupeeBalance
                        binding.etMobileNumber.text.clear()
                        binding.upiId.text.clear()
                        binding.amount.text.clear()
                        Toast.makeText(this@RedeemActivity, response.body()?.data?.status, Toast.LENGTH_SHORT).show()
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
    }

    private fun createAd()
    {
        rewardedAd = MaxRewardedAd.getInstance( "3794b34f9e0dadac", this )
        rewardedAd?.setListener( object : MaxRewardedAdListener
        {
            override fun onAdLoaded(p0: MaxAd?) {
                retryAttempt = 0.0
            }

            override fun onAdDisplayed(p0: MaxAd?) {

            }

            override fun onAdHidden(p0: MaxAd?) {
                rewardedAd?.loadAd()
            }

            override fun onAdClicked(p0: MaxAd?) {

            }

            override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
                // Rewarded ad failed to load
                // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                retryAttempt++
                val delayMillis = TimeUnit.SECONDS.toMillis( Math.pow( 2.0, Math.min( 6.0, retryAttempt ) ).toLong() )

                Handler().postDelayed( { rewardedAd?.loadAd() }, delayMillis )
            }

            override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
                rewardedAd?.loadAd()
            }

            override fun onUserRewarded(p0: MaxAd?, p1: MaxReward?) {

            }

            override fun onRewardedVideoStarted(p0: MaxAd?) {

            }

            override fun onRewardedVideoCompleted(p0: MaxAd?) {
                apiHit()
            }

        })

        rewardedAd?.loadAd()
    }

}