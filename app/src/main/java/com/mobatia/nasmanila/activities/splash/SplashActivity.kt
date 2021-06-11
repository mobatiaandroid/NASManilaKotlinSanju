package com.mobatia.nasmanila.activities.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.activities.tutorial.TutorialActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.constants.manager.AppUtils
import com.mobatia.nasmanila.constants.manager.PreferenceManager
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context = this
        if (AppUtils.checkInternet(context)) {
            getAccessToken(context)
            Handler().postDelayed({
                if (PreferenceManager.getIsFirstLaunch(context as SplashActivity) && PreferenceManager.getUserCode(
                        context as SplashActivity
                    ) == ""
                ) {
                    var tutorialIntent: Intent = Intent(context, TutorialActivity::class.java)
                    tutorialIntent.putExtra("type", 1)
                    startActivity(tutorialIntent)
                    finish()
                } else if (PreferenceManager.getUserCode(context as SplashActivity) == "") {
                    var loginIntent: Intent = Intent(context, LoginActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                } else {
                    var homeIntent: Intent = Intent(context, HomeListActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                }
            }, 5000)
        } else {
            AppUtils.showDialogAlertDismiss(
                context as SplashActivity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred)
        }
    }

    private fun getAccessToken(context: Context) {
        val call: Call<ResponseBody> = ApiClient.getApiService().accessToken(
            PreferenceManager.getUserCode(context)!!
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData =  response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject != null) {
                        val accessToken: String = jsonObject.optString("access_token")
                        PreferenceManager.setAccessToken(context, accessToken)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}