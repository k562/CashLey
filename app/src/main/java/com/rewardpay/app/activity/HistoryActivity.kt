package com.rewardpay.app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.rewardpay.app.BuildConfig
import com.rewardpay.app.R
import com.rewardpay.app.adapter.HistoryRecyclerAdapter
import com.rewardpay.app.adapter.OfferHistoryAdapter
import com.rewardpay.app.databinding.ActivityHistoryBinding
import com.rewardpay.app.model.HistoryData
import com.rewardpay.app.util.MyPreference
import com.rewardpay.app.util.NetworkConfig
import com.rewardpay.app.viewmodel.ViewModelClass

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private var referHistory = ArrayList<HistoryData.Data.ReferHistory>()
    private var offerHistory = ArrayList<HistoryData.Data.OfferHistory>()
    private lateinit var viewModel: ViewModelClass
    private var currentBalance: String? = "00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //back button
        binding.toolBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //app lonin
        AppLovinSdk.getInstance(this).setMediationProvider("max")
        AppLovinSdk.getInstance(this).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            createBannerAd()
        })

        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]

        //get data from api
        getData()

        //redeem now button
        binding.redeemNow.setOnClickListener {
            val intent = Intent(this, RedeemActivity::class.java)
            intent.putExtra("currentBalance", currentBalance)
            startActivity(intent)
        }

        //refer history
        binding.referHistory.setOnClickListener {
            binding.referHistory.setTextColor(resources.getColor(R.color.white))
            binding.offerHistory.setTextColor(resources.getColor(R.color.black))
            binding.referHistory.setBackgroundResource(R.drawable.refer_history_background)
            binding.offerHistory.setBackgroundResource(R.drawable.refer_history_background_white)
            binding.referRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.referRecyclerView.adapter =
                HistoryRecyclerAdapter(referHistory, this)
            if (referHistory.isEmpty()) {
                binding.referRecyclerView.visibility = View.GONE
                binding.noListFound.visibility = View.VISIBLE
            } else {
                binding.referRecyclerView.visibility = View.VISIBLE
                binding.noListFound.visibility = View.GONE
            }
        }

        //offer history
        binding.offerHistory.setOnClickListener {
            binding.referHistory.setTextColor(resources.getColor(R.color.black))
            binding.offerHistory.setTextColor(resources.getColor(R.color.white))
            binding.referHistory.setBackgroundResource(R.drawable.refer_history_background_white)
            binding.offerHistory.setBackgroundResource(R.drawable.refer_history_background)
            binding.referRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.referRecyclerView.adapter =
                OfferHistoryAdapter(offerHistory, this)

            if (offerHistory.isEmpty()) {
                binding.referRecyclerView.visibility = View.GONE
                binding.noListFound.visibility = View.VISIBLE
            } else {
                binding.referRecyclerView.visibility = View.VISIBLE
                binding.noListFound.visibility = View.GONE
            }
        }

        //refresh
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            getData()
        }
    }

    private fun getData() {
        binding.progressBar.visibility = View.VISIBLE
        if (NetworkConfig().networkIsConnected(this)) {
            viewModel.getHistory(
                MyPreference.getUserId(this).toString(),
                MyPreference.getSecurityToken(this),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).observe(this,
                Observer {
                    binding.progressBar.visibility = View.GONE

                    referHistory = it.data.referHistory
                    offerHistory = it.data.offerHistory
                    binding.referAmount.text = it.data.currentBalance.currentBalance
                    binding.referRecyclerView.layoutManager = LinearLayoutManager(this)
                    binding.referRecyclerView.adapter =
                        HistoryRecyclerAdapter(referHistory, this)
                    currentBalance = it.data.currentBalance.currentBalance

                    if (referHistory.isEmpty()) {
                        binding.referRecyclerView.visibility = View.GONE
                        binding.noListFound.visibility = View.VISIBLE
                    } else {
                        binding.referRecyclerView.visibility = View.VISIBLE
                        binding.noListFound.visibility = View.GONE
                    }

                    binding.referHistory.setTextColor(resources.getColor(R.color.white))
                    binding.offerHistory.setTextColor(resources.getColor(R.color.black))
                    binding.referHistory.setBackgroundResource(R.drawable.refer_history_background)
                    binding.offerHistory.setBackgroundResource(R.drawable.refer_history_background_white)

                })
        } else {
            Toast.makeText(this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun createBannerAd() {
        //adView = MaxAdView("8af0jvghjghjg", this)
        binding.adView.setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(p0: MaxAd?) {
                binding.adView.visibility = View.VISIBLE
            }

            override fun onAdDisplayed(p0: MaxAd?) {

            }

            override fun onAdHidden(p0: MaxAd?) {
                binding.adView.loadAd()
            }

            override fun onAdClicked(p0: MaxAd?) {
                TODO("Not yet implemented")
            }

            override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
                binding.adView.loadAd()
            }

            override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
                binding.adView.loadAd()
            }

            override fun onAdExpanded(p0: MaxAd?) {
                TODO("Not yet implemented")
            }

            override fun onAdCollapsed(p0: MaxAd?) {
                TODO("Not yet implemented")
            }

        })

        // Load the ad
        binding.adView.loadAd()
        binding.adView.startAutoRefresh()
    }
}