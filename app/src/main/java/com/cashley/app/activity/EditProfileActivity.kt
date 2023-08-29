package com.cashley.app.activity

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.cashley.app.BuildConfig
import com.cashley.app.R
import com.cashley.app.databinding.ActivityEditProfileBinding
import com.cashley.app.util.MyPreference
import com.cashley.app.util.NetworkConfig
import com.cashley.app.viewmodel.ViewModelClass
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: ViewModelClass
    private lateinit var radio: RadioButton
    private var occupation: String = ""
    private var gender: String = ""
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //app lovin
        AppLovinSdk.getInstance( this).setMediationProvider( "max" )
        AppLovinSdk.getInstance( this ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            createBannerAd()
        })

        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]

        binding.userName.text = MyPreference.getUserName(this)
        binding.userEmail.text = MyPreference.getUserEmail(this)
        Picasso.get().load(MyPreference.getUserImage(this)).placeholder(R.drawable.ic_profile_icon)
            .error(R.drawable.ic_profile_icon).into(binding.userImage)

        //back button
        binding.toolBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //gender
        // Get radio group selected item using on checked change listener
        binding.group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radio = findViewById(checkedId)
                gender = radio.text.toString()
            })

        //select occupation
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_text, resources.getStringArray(R.array.occupation)
        )
        binding.occupation.adapter = adapter

        binding.occupation.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                view?.setPadding(0,0,0,0)
                if (parent?.getItemAtPosition(position).toString() != "Select") {
                    occupation = parent?.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        //date of birth
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        binding.dateOfBirth.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //getData
        getData()

        //save
        binding.SaveButton.setOnClickListener {
            save()
        }

        //refresh
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing=false
            getData()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateOfBirth.text = sdf.format(cal.getTime())
    }

    fun save() {
        if (NetworkConfig().networkIsConnected(this)) {
            viewModel.getEditProfile(
                MyPreference.getUserId(this).toString(),
                MyPreference.getSecurityToken(this),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString(),
                "post",
                binding.mobile.text.toString(),
                binding.dateOfBirth.text.toString(),
                gender,
                occupation
            ).observe(this, Observer {
                it
                Toast.makeText(this, "Update Successfully.", Toast.LENGTH_SHORT).show()
            })
        } else {
            Toast.makeText(this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun getData() {
        binding.progressBar.visibility = View.VISIBLE
        if (NetworkConfig().networkIsConnected(this)) {
            viewModel.getEditProfile(
                MyPreference.getUserId(this).toString(),
                MyPreference.getSecurityToken(this),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString(),
                "get",
                "",
                "",
                "",
                ""
            ).observe(this, Observer {
                binding.progressBar.visibility = View.GONE
                binding.mobile.setText(it.data.mobile)
                binding.dateOfBirth.text = it.data.dateOfBirth

                binding.occupation.setSelection(
                    resources.getStringArray(R.array.occupation).indexOf(it.data.occupation)
                )

                if (it.data.gender.lowercase() == "male") {
                    binding.male.isChecked = true
                    binding.male.requestFocus()
                } else if (it.data.gender.lowercase() == "female") {
                    binding.female.isChecked = true
                    binding.female.requestFocus()
                } else if (it.data.gender.lowercase() == "other") {
                    binding.other.isChecked = true
                    binding.other.requestFocus()
                }
            })
        } else {
            Toast.makeText(this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun createBannerAd()
    {
        //adView = MaxAdView("8af0jvghjghjg", this)
        binding.adView.setListener(object : MaxAdViewAdListener{
            override fun onAdLoaded(p0: MaxAd?) {
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