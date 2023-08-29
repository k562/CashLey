package com.cashley.app.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.cashley.app.BuildConfig
import com.cashley.app.R
import com.cashley.app.databinding.ActivityLogin2Binding
import com.cashley.app.util.MyPreference
import com.cashley.app.util.NetworkConfig
import com.cashley.app.viewmodel.ViewModelClass
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.net.URLDecoder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoginActivity2 : AppCompatActivity(), InstallReferrerStateListener {
    private lateinit var binding: ActivityLogin2Binding
    private lateinit var auth: FirebaseAuth
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var mReferrerClient: InstallReferrerClient
    private lateinit var myGid: String

    //we need this for passing in singUpApi
    private lateinit var deviceId: String
    private lateinit var deviceType: String
    private lateinit var deviceName: String

    //user's social ID
    private lateinit var socialType: String

    //    private lateinit var socialId: String
    private lateinit var socialToken: String

    //user's name,email,image
    private lateinit var socialEmail: String
    private lateinit var socialName: String
    private lateinit var socialImgUrl: String

    //user's advertisingId , versionName, versionCode
//    private  var advertisingId: String?=""
    private lateinit var versionName: String
    private lateinit var versionCode: String

    private var userId: Int = -1
    private var securityToken: String? = null

    //user's utm
    private var utmSource: String = ""
    private var utmMedium: String = ""
    private var utmTerm: String = ""
    private var utmContent: String = ""
    private var utmCompaign: String = ""

    private var referrerLink: String = ""

    private lateinit var viewModel: ViewModelClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setUtm()
        getAdvertisingId()
        viewModel = ViewModelProvider(this)[ViewModelClass::class.java]

        //firebase setup
        FirebaseApp.initializeApp(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        binding.googleLoginButton.setOnClickListener {
//            val intent=Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish
            if(NetworkConfig().networkIsConnected(this)) {
                signIn()
            }
            else
            {
                Toast.makeText(this,"Please Check Your Internet Connection.",Toast.LENGTH_SHORT).show()
            }
        }

        //privacy policy
        binding.txtPrivacy.setOnClickListener {
            openTab("https://www.google.com/")
        }

        //terms & conditions
        binding.txtTermcondition.setOnClickListener {
            openTab("https://www.google.com/")
        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            binding.progressBar.visibility=View.GONE
            Toast.makeText(this, "signInResult:failed code=" + e.statusCode, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val firebaseCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUI(account)
                } else {
                    // If sign in fails, display a message to the user.
                    binding.progressBar.visibility=View.GONE
                    Toast.makeText(
                        this,
                        "Something went wrong please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        if (account != null) {
            //signUpApi
            signUp(account)
        }
    }

    private fun signUp(user: GoogleSignInAccount) {
        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID).toString()
        deviceType = Build.MODEL.toString()
        deviceName = Build.MANUFACTURER + " " + Build.MODEL.toString()
        socialType = "Google"
        socialToken = user.idToken.toString()
        socialName = user.displayName.toString()
        socialEmail = user.email.toString()
        socialImgUrl = user.photoUrl.toString()
        versionName = BuildConfig.VERSION_NAME
        versionCode = BuildConfig.VERSION_CODE.toString()

        binding.progressBar.visibility= View.VISIBLE

        if(NetworkConfig().networkIsConnected(this)) {
            //api hit
            viewModel.signUp(
                deviceId,
                deviceType,
                deviceName,
                socialType,
                user.id.toString(),
                socialToken,
                socialEmail,
                socialName,
                socialImgUrl,
                myGid,
                versionName,
                versionCode,
                utmSource,
                utmMedium,
                utmTerm,
                utmContent,
                utmCompaign,
                referrerLink
            ).observe(this, Observer {

                userId = it.userId
                securityToken = it.securityToken

                MyPreference.saveIdToken(
                    this,
                    it.userId,
                    it.securityToken
                )

                //hit appOpen api
                appOpen(it.userId, it.securityToken)
            })
        }
        else
        {
            binding.progressBar.visibility=View.GONE
            Toast.makeText(this,"Please Check Your Internet Connection.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUtm() {
        mReferrerClient = InstallReferrerClient.newBuilder(this).build()
        mReferrerClient.startConnection(this)
    }

    companion object {
        private const val RC_SIGN_IN = 101
    }

    override fun onInstallReferrerSetupFinished(p0: Int) {
        when (p0) {
            InstallReferrerClient.InstallReferrerResponse.OK ->                 // Connection established
                try {
                    getReferralUser()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {}
            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {}
            InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {}
            InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {}
        }
    }

    private fun getReferralUser() {
        try {
            val response: ReferrerDetails = mReferrerClient.installReferrer
            val referrerData = response.installReferrer
            referrerLink = referrerData.toString()
            Log.d("LINK_", referrerLink)
            Log.d("TAG12122", "Install referrer: $referrerData")
            Log.d("TAG1212", "Install referrer: $referrerLink")

            // Parse referrer data for UTM parameters
            val values = HashMap<String, String>()
            referrerData?.split("&")?.forEach { referrerValue ->
                val keyValue = referrerValue.split("=").toTypedArray()
                if (keyValue.size == 2) {
                    values[URLDecoder.decode(keyValue[0], "UTF-8")] =
                        URLDecoder.decode(keyValue[1], "UTF-8")
                }
            }

            utmSource = values["utm_source"].toString()
            utmMedium = values["utm_medium"].toString()
            utmTerm = values["utm_term"].toString()
            utmCompaign = values["utm_campaign"].toString()
            utmContent = values["utm_content"].toString()

            Log.d(
                "API_UTM",
                "utmSource: $utmSource, utmMedium: $utmMedium, utmTerm: $utmTerm, utmCompaign: $utmCompaign, utmContent: $utmContent"
            )
        } catch (e: Exception) {
            Log.e("TAG1", "Failed to get install referrer: ${e.message}")
        }
    }

    override fun onInstallReferrerServiceDisconnected() {

    }

    private fun getAdvertisingId() {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this)
                myGid = adInfo.id.toString()
                //myGid[0] = adInfo != null ? adInfo.getId() : null;

            } catch (e: Exception) {
                Log.e("error", e.toString())
            }
        }
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
                    binding.progressBar.visibility = View.GONE
                    Log.d("APP_DONE", "HURRY")
                    MyPreference.saveAppOpenDetails(
                        this,
                        it.data.socialEmail,
                        it.data.forceUpdate,
                        it.data.socialImgUrl,
                        it.data.socialName,
                        it.data.appUrl
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }
        else
        {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show()
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
        customBuilder.launchUrl(this, Uri.parse(url))
    }

    override fun onPause() {
        binding.progressBar.visibility=View.GONE
        super.onPause()
    }

    override fun onDestroy() {
        binding.progressBar.visibility=View.GONE
        super.onDestroy()
    }
}