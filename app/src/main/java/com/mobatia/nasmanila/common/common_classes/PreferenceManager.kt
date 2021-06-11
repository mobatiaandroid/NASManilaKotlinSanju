package com.mobatia.nasmanila.common.common_classes

import android.content.Context
import android.content.SharedPreferences
import com.mobatia.nasmanila.activities.splash.SplashActivity

class PreferenceManager {
    companion object {
        private const val sharedPrefNas = "NAS_MANILA"
        fun getUserCode(context: Context): String? {
            var userCode: String
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            userCode = sharedPreferences.getString("user_code", "").toString()
            return userCode
        }

        fun setIsFirstLaunch(context: Context, result: Boolean) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("is_first_launch", result)
            editor.apply()
        }
        fun getIsFirstLaunch(context: SplashActivity): Boolean {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("is_first_launch", true)
        }

        fun setAccessToken(context: Context, accessToken: String) {
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("access_token", accessToken)
            editor.apply()
        }
        fun getAccessToken(context: Context?): String {
            val tokenValue: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            tokenValue = sharedPreferences.getString("access_token", "").toString()
            return tokenValue
        }

        fun setUserID(context: Context, userID: String) {
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("userId", userID)
            editor.apply()
        }


    }
}