package com.rewardpay.app.fragments

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.rewardpay.app.BuildConfig
import com.rewardpay.app.R
import com.rewardpay.app.databinding.FragmentInviteBinding
import com.rewardpay.app.util.MyPreference
import com.rewardpay.app.util.NetworkConfig
import com.rewardpay.app.util.NetworkConnection
import com.rewardpay.app.viewmodel.ViewModelClass


class InviteFragment : Fragment() {

    private lateinit var binding: FragmentInviteBinding
    lateinit var aContext: Context
    private lateinit var viewModel: ViewModelClass
    private lateinit var other: String
    private lateinit var facebook: String
    private lateinit var telegram: String
    private lateinit var whatsapp: String

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
        binding = FragmentInviteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]

        //back button
        binding.toolBar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        //appLovin
        AppLovinSdk.getInstance(activity).setMediationProvider("max")
        AppLovinSdk.getInstance(activity).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            createBannerAd()
        })

//        api hit
        apiHit()

        //copy button
        binding.referCodeButton.setOnClickListener {
            val clipboard = aContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", binding.copy.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(aContext, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
        }

        //facebook
        binding.facebook.setOnClickListener {
            shareOnFacebook()
        }

        //whatsApp
        binding.whatsApp.setOnClickListener {
            shareOnWhatsapp()
        }

        //telegram
        binding.telegram.setOnClickListener {
            shareOnTelegram()
        }

        //more
        binding.more.setOnClickListener {
            shareAppOther()
        }

        //refresh
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            apiHit()
        }

    }

    fun shareOnWhatsapp() {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, whatsapp)
        try {
            this.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(aContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareOnTelegram() {
        val appName = "org.telegram.messenger"
        if (isAppAvailable(appName)) {
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "text/plain"
            myIntent.setPackage(appName)
            myIntent.putExtra(Intent.EXTRA_TEXT, telegram)
            startActivity(Intent.createChooser(myIntent, "Share with"))

        } else {
            Toast.makeText(aContext, "Telegram Not Installed.", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareOnFacebook() {
        val facebookIntent = Intent(Intent.ACTION_SEND)
        facebookIntent.type = "text/plain"
        facebookIntent.setPackage("com.facebook.katana")
        facebookIntent.putExtra(Intent.EXTRA_TEXT, facebook)
        try {
            this.startActivity(facebookIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(aContext, "Facebook have not been installed.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun shareAppOther() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            val shareMessage = "\nLet me recommend you this application\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, other)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    fun isAppAvailable(appName: String): Boolean {
        //
        try {
            aContext.packageManager.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return false
        }
    }

    private fun apiHit() {
        binding.progressBar.visibility = View.VISIBLE
        binding.lottieAnimation.pauseAnimation()

//        NetworkConnection(aContext).observe(requireActivity(), Observer { isConnected ->
//            if (isConnected) {
//                viewModel.getInvite(
//                    MyPreference.getUserId(aContext).toString(),
//                    MyPreference.getSecurityToken(aContext),
//                    BuildConfig.VERSION_NAME,
//                    BuildConfig.VERSION_CODE.toString()
//                ).observe(requireActivity(),
//                    Observer {
//                        binding.lottieAnimation.playAnimation()
//                        binding.progressBar.visibility = View.GONE
//                        binding.copy.text = it.data.referCode
//                        binding.perRefer.text = "Per Refer Coins " + it.data.perRefer
//                        binding.tvOfferAFriend.text = it.data.description
//                        other = it.data.other
//                        facebook = it.data.facebook
//                        telegram = it.data.telegram
//                        whatsapp = it.data.telegram
//                    })
//            }
//            else
//            {
//                Toast.makeText(
//                    aContext,
//                    "Please Check Your Internet Connection.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })

        if (NetworkConfig().networkIsConnected(aContext)) {
            viewModel.getInvite(
                MyPreference.getUserId(aContext).toString(),
                MyPreference.getSecurityToken(aContext),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).observe(requireActivity(),
                Observer {
                    binding.lottieAnimation.playAnimation()
                    binding.progressBar.visibility = View.GONE
                    binding.copy.text = it.data.referCode
                    binding.perRefer.text = "Per Refer Coins " + it.data.perRefer
                    binding.tvOfferAFriend.text = it.data.description
                    other = it.data.other
                    facebook = it.data.facebook
                    telegram = it.data.telegram
                    whatsapp = it.data.telegram
                })
        } else {
            Toast.makeText(
                aContext,
                "Please Check Your Internet Connection.",
                Toast.LENGTH_SHORT
            )
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
            }

            override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
                binding.adView.loadAd()
            }

            override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
                binding.adView.loadAd()
            }

            override fun onAdExpanded(p0: MaxAd?) {
            }

            override fun onAdCollapsed(p0: MaxAd?) {
            }

        })

        // Load the ad
        binding.adView.loadAd()
        binding.adView.startAutoRefresh()
    }

}