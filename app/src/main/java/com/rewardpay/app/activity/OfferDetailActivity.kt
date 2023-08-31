package com.rewardpay.app.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.rewardpay.app.BuildConfig
import com.rewardpay.app.R
import com.rewardpay.app.databinding.ActivityOfferDetailBinding
import com.rewardpay.app.util.MyPreference
import com.rewardpay.app.util.NetworkConfig
import com.rewardpay.app.viewmodel.ViewModelClass
import com.squareup.picasso.Picasso

class OfferDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOfferDetailBinding
    private lateinit var viewModel: ViewModelClass
    private var offerId: String? = null
    private lateinit var offerLink: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent != null) {
            offerId = intent.getStringExtra("offerId")
        }

        //app lonin
        AppLovinSdk.getInstance( this).setMediationProvider( "max" )
        AppLovinSdk.getInstance( this ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            createBannerAd()
        })

        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]
        if(NetworkConfig().networkIsConnected(this)) {
            viewModel.getOfferDetails(
                offerId.toString(),
                MyPreference.getUserId(this).toString(),
                MyPreference.getSecurityToken(this),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).observe(this,
                Observer {
                    binding.progressBar.visibility = View.GONE
                    binding.screenVisiblity.visibility = View.VISIBLE
                    binding.offerType.text = it.data.offerDeatils.offerCategory
                    binding.perRefer.text = "Per Refer Coins " + it.data.offerDeatils.perRefer
                    binding.offerName.text = it.data.offerDeatils.offerName
                    binding.amount.text = it.data.offerDeatils.offerAmount
                    Picasso.get().load(it.data.offerDeatils.offerImage)
                        .placeholder(R.drawable.baseline_image_24)
                        .error(R.drawable.baseline_image_24)
                        .into(binding.offerImage)
                    Picasso.get().load(it.data.offerDeatils.offerImage)
                        .placeholder(R.drawable.baseline_image_24)
                        .error(R.drawable.baseline_image_24)
                        .into(binding.offerImage2)
                    binding.description.text = it.data.description.description
                    offerLink = it.data.offerLink.offerLink
                })
        }
        else
        {
            Toast.makeText(this,"Please Check Your Internet Connection.",Toast.LENGTH_SHORT).show()
        }

        binding.installButton.setOnClickListener {
            checkValidation()
        }

        //back button
        binding.toolBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //share button
        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> {
                    shareOffer()
                    true
                }

                else -> {
                    true
                }
            }
        }

    }

    private fun shareOffer() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            val shareMessage = "\nLet me recommend you this application\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, offerLink)

            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    private fun checkValidation()
    {
        binding.progressBar.visibility=View.VISIBLE
        var mobile=binding.etMobileNumber.text.toString()
        var upiId=binding.upiId.text.toString()

        if(mobile.isEmpty() || mobile.length>10 || mobile.length<10)
        {
            binding.etMobileNumber.error="Valid Mobile Number Required"
            binding.etMobileNumber.requestFocus()
            binding.progressBar.visibility=View.GONE
        }
        else if(isUpiIdValid(upiId)==false)
        {
            binding.upiId.error="Valid UPI-Id Required"
            binding.upiId.requestFocus()
            binding.progressBar.visibility=View.GONE
        }
        else
        {
            binding.etMobileNumber.clearFocus()
            binding.upiId.clearFocus()

            if(NetworkConfig().networkIsConnected(this)) {
                viewModel.getInstallLink(
                    offerId.toString(),
                    MyPreference.getUserId(this).toString(),
                    MyPreference.getSecurityToken(this),
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE.toString(),
                    binding.etMobileNumber.text.toString(),
                    binding.upiId.text.toString()
                ).observe(this, Observer {

                    binding.progressBar.visibility = View.GONE
                    binding.etMobileNumber.text.clear()
                    binding.upiId.text.clear()
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(it.offerLink)
                    startActivity(openURL)

                })
            }
            else
            {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this,"Please Check Your Internet Connection.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isUpiIdValid(upiId: String): Boolean {
        // Define a regular expression pattern for UPI IDs
        val regexPattern = "^[\\w.-]+@[\\w.-]+\$"

        // Validate the input against the pattern
        return upiId.matches(Regex(regexPattern))
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun createBannerAd()
    {
        //adView = MaxAdView("8af0jvghjghjg", this)
        binding.adView.setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(p0: MaxAd?) {
                binding.adView.visibility=View.VISIBLE
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