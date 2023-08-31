package com.rewardpay.app.fragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.rewardpay.app.BuildConfig
import com.rewardpay.app.activity.HistoryActivity
import com.rewardpay.app.adapter.FragmentAdapter
import com.rewardpay.app.adapter.ViewPagerAdapter
import com.rewardpay.app.databinding.FragmentHomeBinding
import com.rewardpay.app.model.HomeData
import com.rewardpay.app.util.MyPreference
import com.rewardpay.app.util.NetworkConfig
import com.rewardpay.app.viewmodel.ViewModelClass
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ViewModelClass
    private var handler: Handler? = null
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.
    lateinit var aContext: Context
    var sliderSize: Int? = 0
    private var interstitialAd: MaxInterstitialAd? = null
    private var retryAttempt = 0.0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]

        binding.indicator.clearFocus()

        //adView
        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance(context).setMediationProvider("max")
        AppLovinSdk.getInstance(context).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            createAd()
        })

        //tooBar menu
        toolBarMenu()
        getHomeData()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            requestPermissions(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            getHomeData()
        }

    }

    private fun setupTabLayout() {
        TabLayoutMediator(
            binding.tabLayout, binding.viewpager2
        ) { tab, position ->
            if (position == 0) {
                tab.text = "ALL OFFERS"
            } else {
                tab.text = "HIGH PAYING"
            }
        }.attach()
    }


    private fun setupViewPager(
        allOffers: ArrayList<HomeData.Data.AllOffer>,
        highestPaying: ArrayList<HomeData.Data.HighestPaying>
    ) {
        val adapter = FragmentAdapter(requireActivity())
//        val fragment1:AllOffersFragment?=AllOffersFragment(allOffers)
//        val fragment2:HighPayingFragment?=HighPayingFragment(highestPaying)
        adapter.addFragment(AllOffersFragment(allOffers))
        adapter.addFragment(HighPayingFragment(highestPaying))
        binding.viewpager2.adapter = adapter
    }

    private fun toolBarMenu() {
        binding.toolBarMenu.setOnClickListener {
            val intent = Intent(context, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getHomeData() {
        binding.progressBar.visibility = View.VISIBLE
        if (NetworkConfig().networkIsConnected(requireContext())) {
            viewModel.getHome(
                MyPreference.getUserId(aContext).toString(),
                MyPreference.getSecurityToken(aContext),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).observe(requireActivity(),
                Observer {

                    sliderSize = it.data.slider.size
                    binding.progressBar.visibility = View.GONE

                    binding.currentBalance.text=it.data.currentBalance

                    //set slider
                    val adapter = ViewPagerAdapter(it.data.slider)
                    binding.viewPager.adapter = adapter
                    binding.indicator.setViewPager(binding.viewPager)

                    viewpager()

                    /*After setting the adapter use the timer */
                    /*After setting the adapter use the timer */
//                binding.indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//                    override fun onPageSelected(position: Int) {
////                        currentPage = position
//                    }
//
//                    override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {
//                        currentPage = pos
//                    }
//                    override fun onPageScrollStateChanged(pos: Int) {
////                        currentPage = pos
//                    }
//                })

                    //viewPager

                    //set tab
                    setupViewPager(it.data.allOffers, it.data.highestPaying)
                    setupTabLayout()
                })
        } else {
            Toast.makeText(
                requireContext(),
                "Please Check Your Internet Connection.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun requestPermissions(permission: String) {
        Dexter.withContext(aContext).withPermissions(permission).withListener(object :
            MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                // this method is called when all permissions are granted
                if (p0!!.areAllPermissionsGranted()) {

                }
                // check for permanent denial of any permission
                if (p0.isAnyPermissionPermanentlyDenied) {
                    // permission is denied permanently, we will show user a dialog message.
                    //dialogbox()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1!!.continuePermissionRequest()
            }
        }).withErrorListener {
            Toast.makeText(aContext, "Error occurred! ", Toast.LENGTH_SHORT).show()
        }.onSameThread().check()
    }

    fun dialogbox() {
        val dialogBoxShow = AlertDialog.Builder(aContext)
        dialogBoxShow.apply {
            setTitle("Important")
            setMessage("This feature would not be available as you have denied the permissions.. sorry for inconvenience")
            setPositiveButton("Ok") { dialogInterface, which ->

            }
            create()
            setCancelable(false)
            show()
        }
    }

    private fun createAd() {
        interstitialAd = MaxInterstitialAd("c4c14eb94c68c5f3", activity)
        interstitialAd?.setListener(object : MaxAdListener {
            override fun onAdLoaded(p0: MaxAd?) {
                // Reset retry attempt
                retryAttempt = 0.0
            }

            override fun onAdDisplayed(p0: MaxAd?) {
            }

            override fun onAdHidden(p0: MaxAd?) {
                interstitialAd?.loadAd()
            }

            override fun onAdClicked(p0: MaxAd?) {
            }

            override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
                // Interstitial ad failed to load
                // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                retryAttempt++
                val delayMillis =
                    TimeUnit.SECONDS.toMillis(Math.pow(2.0, Math.min(6.0, retryAttempt)).toLong())

                Handler().postDelayed({ interstitialAd?.loadAd() }, delayMillis)
            }

            override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
                // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
                interstitialAd?.loadAd()
            }

        })

        // Load the first ad
        interstitialAd?.loadAd()
    }

    fun viewpager() {
        if (handler == null) {
            handler = Handler()
            val Update = Runnable {
                if (currentPage == sliderSize) {
                    currentPage = 0
                }
                binding.indicator.clearFocus()
                binding.viewPager.setCurrentItem(currentPage++, true)
            }

            val timer = Timer() // This will create a new Thread

            timer.schedule(object : TimerTask() {
                // task to be scheduled
                override fun run() {
                    handler?.post(Update)
                }
            }, 2500, 7500)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}