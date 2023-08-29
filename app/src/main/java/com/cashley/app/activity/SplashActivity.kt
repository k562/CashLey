package com.cashley.app.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cashley.app.BuildConfig
import com.cashley.app.R
import com.cashley.app.databinding.ActivitySplashBinding
import com.cashley.app.util.MyPreference
import com.cashley.app.util.NetworkConfig
import com.cashley.app.viewmodel.ViewModelClass

class SplashActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    private lateinit var viewModel: ViewModelClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]

        Handler(Looper.getMainLooper()).postDelayed({

            val userId = MyPreference.getUserId(this)
            val securityToken = MyPreference.getSecurityToken(this)
//            startActivity(Intent(this@SplashScreen, MainActivity::class.java))

            if (userId > 0 && securityToken!!.isNotBlank()) {
                // User is already logged in, (appOpen Api hit) navigate to the main activity
                Log.d("APP_OPEN", "userId: $userId, securityToken: $securityToken")

                appOpen(userId, securityToken)
            } else {
                // User is not logged in, navigate to the sign in activity
                val intent = Intent(this, LoginActivity2::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }

    private fun appOpen(userId: Int, securityToken: String) {
        if(NetworkConfig().networkIsConnected(this)) {
            viewModel.appOpen(
                userId.toString(),
                securityToken,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).observe(this, Observer {
                if (it != null) {
                    Log.d("APP_DONE", "HURRY")
                    MyPreference.saveAppOpenDetails(
                        this,
                        it.data.socialEmail,
                        it.data.forceUpdate,
                        it.data.socialImgUrl,
                        it.data.socialName,
                        it.data.appUrl
                    )
                    if (it.data.forceUpdate) {
                        showForceUpdateDialog(it.data.appUrl)
                    } else {
                        Log.d(
                            "USER_DETAILS",
                            "${it.data.socialEmail}, ${it.data.forceUpdate}, ${it.data.socialImgUrl}, ${it.data.socialName}, ${it.data.appUrl}"
                        )
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }
        else
        {
            Toast.makeText(this,"Please Check Your Internet Connection.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showForceUpdateDialog(appUrl: String?) {
        if (appUrl != null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Update Required")
            builder.setMessage("A new version of the app is available. Please update to continue using the app.")
            builder.setCancelable(false)
            builder.setPositiveButton("Update") { _, _ ->
                // Handle update button click
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(appUrl)
                startActivity(intent)
            }

            val dialog = builder.create()
            dialog.show()
        }
    }
}