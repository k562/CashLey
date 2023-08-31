package com.rewardpay.app.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.rewardpay.app.R
import com.rewardpay.app.activity.EditProfileActivity
import com.rewardpay.app.databinding.FragmentProfileBinding
import com.rewardpay.app.util.MyPreference
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //back button
        binding.toolBar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        //app lonin
        AppLovinSdk.getInstance( activity).setMediationProvider( "max" )
        AppLovinSdk.getInstance( activity ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            createBannerAd()
        })

        binding.userName.text=MyPreference.getUserName(context)
        binding.userEmail.text=MyPreference.getUserEmail(context)
        Picasso.get().load(MyPreference.getUserImage(context)).placeholder(R.drawable.ic_profile_icon).error(R.drawable.ic_profile_icon).into(binding.userImage)

        binding.editProfile.setOnClickListener {
            val intent= Intent(context,EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.llPrivacyPolicy.setOnClickListener {
            openTab("https://www.google.com")
        }

        binding.llTermServices.setOnClickListener {
            openTab("https://www.google.com")
        }

        binding.llAboutUs.setOnClickListener {
            openTab("https://www.google.com")
        }

        //whatsapp
        binding.whatsApp.setOnClickListener {
            openTab("https://chat.whatsapp.com/L05qVKjYr1lKLI8LAGYLfr")
        }

        //instagram
        binding.instagram.setOnClickListener {
            openTab("https://www.instagram.com/sanju_gamer__ff/")
        }

        //telegram
        binding.telegram.setOnClickListener {
            openTab("https://t.me/EarnMoreEarnBig")
        }

        //facebook
        binding.facebook.setOnClickListener {
            openTab("https://www.facebook.com/gamersanjuyt")
        }

        //twitter
        binding.twitter.setOnClickListener {
//            openTab("https://www.google.com")
            Toast.makeText(context,"This feature will come in soon.",Toast.LENGTH_SHORT).show()
        }

        //youtube
        binding.youtube.setOnClickListener {
            openTab("https://www.youtube.com/@gamerewards6689")
        }
    }

    fun openTab(url:String)
    {
        val builder = CustomTabsIntent.Builder()

        // to set the toolbar color use CustomTabColorSchemeParams
        // since CustomTabsIntent.Builder().setToolBarColor() is deprecated

//        val params = CustomTabColorSchemeParams.Builder()
//        params.setToolbarColor(ContextCompat.getColor(this, R.color.custom_theme_color))
//        builder.setDefaultColorSchemeParams(params.build())
//        // shows the title of web-page in toolbar
//        builder.setShowTitle(true)

        // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

        // To modify the close button, use
        // builder.setCloseButtonIcon(bitmap)

        // to set weather instant apps is enabled for the custom tab or not, use
        builder.setInstantAppsEnabled(true)

        //  To use animations use -
        //  builder.setStartAnimations(this, android.R.anim.start_in_anim, android.R.anim.start_out_anim)
        //  builder.setExitAnimations(this, android.R.anim.exit_in_anim, android.R.anim.exit_out_anim)
        val customBuilder = builder.build()
        customBuilder.intent.setPackage("com.android.chrome")
        customBuilder.launchUrl(requireContext(), Uri.parse(url))
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