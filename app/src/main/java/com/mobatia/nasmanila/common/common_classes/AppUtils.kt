package com.mobatia.nasmanila.common.common_classes

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.net.ConnectivityManager
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.constants.JSONConstants
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppUtils {
    var progressBarDialog: ProgressBarDialog? = null

    companion object {
        fun getAccessToken(context: Context) {
            val call: Call<ResponseBody> = ApiClient.getApiService().accessToken(
                PreferenceManager.getUserCode(context)!!
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        val jsonObject = JSONObject(responseData.string())
                        if (jsonObject != null) {
                            val accessToken: String = jsonObject.optString("authorization-user")
                            PreferenceManager.setAccessToken(context, accessToken)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        fun showDialogAlertDismiss(
            context: Context?,
            msgHead: String?,
            msg: String?,
            ico: Int,
            bgIcon: Int
        ) {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
            icon.setBackgroundResource(bgIcon)
            icon.setImageResource(ico)
            val text = dialog.findViewById<View>(R.id.textDialog) as TextView
            val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
            text.text = msg
            textHead.text = msgHead
            val dialogButton = dialog.findViewById<View>(R.id.btnOK) as Button
            dialogButton.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }

        fun checkInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }

        fun getButtonDrawableByScreenCategory(
            context: Context?,
            normalStateResID: Int,
            pressedStateResID: Int
        ): Drawable {
            val stateNormal: Drawable = context?.resources?.getDrawable(normalStateResID)!!.mutate()
            val statePressed: Drawable =
                context?.resources?.getDrawable(pressedStateResID)!!.mutate()
            val drawable = StateListDrawable()
            drawable.addState(intArrayOf(android.R.attr.state_pressed), statePressed)
            drawable.addState(intArrayOf(android.R.attr.state_enabled), stateNormal)
            return drawable
        }

        fun hideKeyboard(context: Context?) {
//        if (editText != null) {
//            val imm = context
//                    ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(editText.windowToken, 0)
//        }
            val imm = context
                ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if (imm.isAcceptingText) {

                imm.hideSoftInputFromWindow(
                    (context as Activity).currentFocus
                        ?.windowToken, 0
                )
            }
        }

        fun isValidEmail(string: String): Boolean {
            return return Patterns.EMAIL_ADDRESS.matcher(string).matches()
        }

        fun replace(s: String): String {
            return s.replace(" ".toRegex(), "%20")
        }

        fun showDialogAlertLogout(
            activity: FragmentActivity?,
            s: String,
            s1: String,
            questionMarkIcon: Int,
            round: Int
        ) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_layout)
            val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
            icon.setBackgroundResource(round)
            icon.setImageResource(questionMarkIcon)
            val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
            val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
            text.text = s
            textHead.text = s1

            val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
            dialogButton.setOnClickListener {
                if (PreferenceManager.getUserCode(activity)
                        .equals("", ignoreCase = true)
                ) {
                    PreferenceManager.setUserCode(activity)
                    dialog.dismiss()
                    val mIntent = Intent(activity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    activity.startActivity(mIntent)
                } else {
                    callLogoutApi(activity, dialog)
                }
            }
            val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
            dialogButtonCancel.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }

        var progressBarDialog: ProgressBarDialog? = null
        fun callLogoutApi(activity: Activity, dialog: Dialog) {
            val call: Call<ResponseBody> = ApiClient.getApiService().logOut()
            progressBarDialog!!.show()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressBarDialog!!.dismiss()
                    val responseData = response.body()
                    if (responseData != null) {
                        val jsonObject = JSONObject(responseData.string())
                        if (jsonObject.has("status")) {
                            val status = jsonObject.optInt("status")
                            if (status == 100) {
                                dialog.dismiss()
                                PreferenceManager.setUserCode(activity)
                                val mIntent = Intent(activity, LoginActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                activity.startActivity(mIntent)
                            } else {
                                dialog.dismiss()
                                showDialogAlertDismiss(
                                    activity,
                                    "Alert",
                                    activity.getString(R.string.common_error),
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }

                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressBarDialog!!.dismiss()
                }

            })

        }
    }
}