package com.mobatia.nasmanila.activities.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.iid.FirebaseInstanceId
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var mUserNameEdtTxt: EditText
    lateinit var mPasswordEdtTxt: EditText
    lateinit var mNeedpasswordBtn: Button
    lateinit var mGuestUserButton: Button
    lateinit var mLoginBtn: Button
    lateinit var mSignUpBtn: Button
    lateinit var mMailEdtText: EditText
    lateinit var mHelpButton: Button
    lateinit var mProgressBar: ProgressBar
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext = this
        PreferenceManager.setIsFirstLaunch(mContext as LoginActivity, false)
        initialiseUI()
        setListeners()
    }

    private fun initialiseUI() {
        mProgressBar = (findViewById<View>(R.id.progressBar) as ProgressBar?)!!
        mUserNameEdtTxt = findViewById<View>(R.id.userEditText) as EditText
        dialog = Dialog(mContext, R.style.NewDialog)
        mProgressBar = findViewById(R.id.progressBar)
        mUserNameEdtTxt.setOnEditorActionListener { v, actionId, event ->
            mUserNameEdtTxt.isFocusable = false
            mUserNameEdtTxt.isFocusableInTouchMode = false
            false
        }
        mPasswordEdtTxt = findViewById<View>(R.id.passwordEditText) as EditText
        mPasswordEdtTxt.setOnEditorActionListener { v, actionId, event ->
            mPasswordEdtTxt.isFocusable = false
            mPasswordEdtTxt.isFocusableInTouchMode = false
            false
        }
        mHelpButton = findViewById<View>(R.id.helpButton) as Button
        mNeedpasswordBtn = findViewById<View>(R.id.forgotPasswordButton) as Button
        mGuestUserButton = findViewById<View>(R.id.guestButton) as Button
        mLoginBtn = findViewById<View>(R.id.loginBtn) as Button
        mSignUpBtn = findViewById<View>(R.id.signUpButton) as Button

        mNeedpasswordBtn.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                mContext,
                R.drawable.forgotpassword,
                R.drawable.forgotpasswordpress
            )
        )
        mGuestUserButton.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                mContext,
                R.drawable.guest,
                R.drawable.guestpress
            )
        )
        mLoginBtn.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                mContext,
                R.drawable.login,
                R.drawable.loginpress
            )
        )
        mSignUpBtn.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                mContext,
                R.drawable.signup_new,
                R.drawable.signuppress_new
            )
        )
        mHelpButton.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                mContext,
                R.drawable.help,
                R.drawable.helppress
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        mUserNameEdtTxt.setOnTouchListener { v, event ->
            mUserNameEdtTxt.isFocusable = true
            mUserNameEdtTxt.isFocusableInTouchMode = true
            false
        }
        mPasswordEdtTxt.setOnTouchListener { v, event ->
            mPasswordEdtTxt.isFocusable = true
            mPasswordEdtTxt.isFocusableInTouchMode = true
            false
        }
        mLoginBtn.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            if (mUserNameEdtTxt.text.toString().trim().equals("", ignoreCase = true))
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            else if (!AppUtils.isValidEmail(mUserNameEdtTxt?.text.toString()))
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_valid_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            else if (mPasswordEdtTxt.text.toString().equals("", ignoreCase = true))
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_password
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            else
                loginApiCall()
        }
        mGuestUserButton.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            PreferenceManager.setUserID(mContext, "")
            var homeIntent: Intent = Intent(mContext, HomeListActivity::class.java)
            startActivity(homeIntent)
        }
        mSignUpBtn.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            if (mContext?.let { AppUtils.checkInternet(it) })
                showSignUpAlertDialog()
            else
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?, "Network Error", getString(
                        R.string.no_internet
                    ), R.drawable.nonetworkicon, R.drawable.roundred
                )
        }
        mHelpButton.setOnClickListener { v ->
            var emailIntent: Intent = Intent(Intent.ACTION_SEND)
            var deliveryAddress = "appsupport@naismanila.edu.ph"
    //            emailIntent.putExtra(Intent.EXTRA_EMAIL, Uri.parse(deliveryAddress))
    //            emailIntent.data = Uri.parse("mailto:")
    //            emailIntent.type = "text/plain"
    //            emailIntent.putExtra(Intent.EXTRA_EMAIL, "appsupport@naismanila.edu.ph")
    //            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "The subject");
    //            emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body");
    //            emailIntent.putExtra(Intent.EXTRA_CC,"sabusanju2@gmail.com")
    //            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    //            var packageManager: PackageManager = v!!.context.packageManager
    //            v.context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$deliveryAddress"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
            intent.putExtra(Intent.EXTRA_TEXT, "message")
            v.context.startActivity(intent)
    //            val activityList: List<ResolveInfo> = packageManager.queryIntentActivities(emailIntent, 0)
    //            for (app in activityList) {
    //                if (app.activityInfo.name.contains("com.google.android.gm")) {
    //                    val activity = app.activityInfo
    //                    val name = ComponentName(
    //                            activity.applicationInfo.packageName,
    //                            activity.name
    //                    )
    //                    emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    //                    emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
    //                            or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    //                    emailIntent.component = name
    //                    v.context.startActivity(emailIntent)
    //                    break
    //                }
    //            }
        }
        mNeedpasswordBtn.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            if (mContext.let { AppUtils.checkInternet(it) })
                forgotPasswordApiCall()
            else
                AppUtils.showDialogAlertDismiss(
                    mContext as Context, "Network Error", getString(
                        R.string.no_internet
                    ), R.drawable.nonetworkicon, R.drawable.roundred
                )
        }
    }

    private fun forgotPasswordApiCall() {
        val dialog: Dialog = Dialog(mContext!!, R.style.NewDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_forgot_password)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        mMailEdtText = dialog.findViewById<View>(R.id.text_dialog) as EditText
//        mMailEdtText!!.setOnTouchListener(this)
        val alertHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        val dialogSubmitButton = dialog.findViewById<View>(R.id.btn_signup) as Button
        dialogSubmitButton.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            if (!mMailEdtText!!.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
                if (AppUtils.isValidEmail(mMailEdtText!!.text.toString())) {
                    if (AppUtils.checkInternet(mContext!!))
                        sendForgotPassword()
                    else
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?, "Network Error", getString(
                                R.string.no_internet
                            ), R.drawable.nonetworkicon, R.drawable.roundred
                        )
                    dialog.dismiss()
                } else
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?, getString(R.string.alert_heading), getString(
                            R.string.invalid_email
                        ), R.drawable.exclamationicon, R.drawable.round
                    )
            } else
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
        }
        val cancelButton = dialog.findViewById<View>(R.id.button2) as Button
        cancelButton.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendForgotPassword() {
        val call: Call<ResponseBody> = ApiClient.getApiService().forgotPassword(
            mMailEdtText.text.toString()
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status: Int = jsonObject.optInt("status")
                        val pass: String = jsonObject.optString("pass")

                        if (status == 100) {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?, "Success", getString(
                                    R.string.frgot_success_alert
                                ), R.drawable.tick, R.drawable.round
                            )
                        } else if (status == 114) {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?, "Success",
                                "User not found in our database"
                                    , R.drawable.tick, R.drawable.round
                            )
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?, "Alert", mContext!!.getString(
                                    R.string.common_error
                                ), R.drawable.exclamationicon, R.drawable.round
                            )
                        }
                    }
                }
//                val responseData = response.body()
//                if (responseData != null) {
//                val responseString = response.body()!!.string()
//                val jsonObject = JSONObject(responseString)
//                val statusCode = jsonObject.getString("status")
//                if (statusCode.equals("100")) {
//                    val responseJSONObject = jsonObject.getJSONObject(JTAG_RESPONSE)
//                    val statusCode = responseJSONObject.getString(JTAG_STATUSCODE)
//                    if (statusCode.equals("303", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, "Success", getString(
//                                R.string.frgot_success_alert
//                            ), R.drawable.tick, R.drawable.round
//                        )
//                    } else if (statusCode.equals("301", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.missing_parameter
//                            ), R.drawable.infoicon, R.drawable.round
//                        )
//                    } else if (statusCode.equals("304", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.email_exists
//                            ), R.drawable.infoicon, R.drawable.round
//                        )
//                    } else if (statusCode.equals("305", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.incrct_usernamepswd
//                            ), R.drawable.exclamationicon, R.drawable.round
//                        )
//                    } else if (statusCode.equals("306", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.invalid_email
//                            ), R.drawable.exclamationicon, R.drawable.round
//                        )
//                    } else {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.common_error
//                            ), R.drawable.exclamationicon, R.drawable.round
//                        )
//                    }
//                } else if (statusCode.equals("500", ignoreCase = true)) {
//                    appUtils.showDialogAlertDismiss(
//                        mContext as Activity?, "Alert", mContext!!.getString(
//                            R.string.common_error
//                        ), R.drawable.exclamationicon, R.drawable.round
//                    )
//                } else if (statusCode.equals("400", ignoreCase = true)) {
//                    appUtils.getToken(mContext!!)
//                    sendForgotPassword()
//                } else if (statusCode.equals("401", ignoreCase = true)) {
//                    appUtils.getToken(mContext!!)
//                    sendForgotPassword()
//                } else if (statusCode.equals("402", ignoreCase = true)) {
//                    appUtils.getToken(mContext!!)
//                    sendForgotPassword()
//                } else
//                    appUtils.showDialogAlertDismiss(
//                        mContext as Activity?, "Alert", mContext!!.getString(
//                            R.string.common_error
//                        ), R.drawable.exclamationicon, R.drawable.round
//                    )
////                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

    private fun showSignUpAlertDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout_signup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        mMailEdtText = dialog.findViewById<View>(R.id.text_dialog) as EditText
        val dialogSubmitButton = dialog.findViewById<View>(R.id.btn_signup) as Button
        dialogSubmitButton.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            if (!mMailEdtText!!.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
                if (AppUtils.isValidEmail(mMailEdtText!!.text.toString())) {
                    if (AppUtils.checkInternet(mContext!!))
                        sendSignUpRequest()
                    else
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?, "Network Error", getString(
                                R.string.no_internet
                            ), R.drawable.nonetworkicon, R.drawable.roundred
                        )
                }
                else
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?, getString(R.string.alert_heading), getString(
                            R.string.enter_valid_email
                        ), R.drawable.exclamationicon, R.drawable.round
                    )
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            }
        }

        val maybeLaterButton = dialog.findViewById<View>(R.id.button2) as Button
        maybeLaterButton.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendSignUpRequest() {
//        val call: Call<ResponseBody> = ApiClient.getApiService().signUp()
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
////                val responseData = response.body()
////                if (responseData != null)
//                    val responseString = response.body()!!.string()
//                    val jsonObject = JSONObject(responseString)
//                    val responseCode: String = jsonObject.getString(JTAG_RESPONSECODE)
//                    if (responseCode.equals("200", ignoreCase = true)) {
//                        val responseJSONObject: JSONObject = jsonObject.getJSONObject(JTAG_RESPONSE)
//                        val statusCode = responseJSONObject.getString(JTAG_STATUSCODE)
//                        if (statusCode.equals("303", ignoreCase = true)) {
//                            dialog.dismiss()
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                "Success",
//                                getString(R.string.signup_success_alert),
//                                R.drawable.tick,
//                                R.drawable.round
//                            )
//                        } else if (statusCode.equals("301", ignoreCase = true)) {
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                getString(R.string.error_heading),
//                                getString(R.string.missing_parameter),
//                                R.drawable.infoicon,
//                                R.drawable.round
//                            )
//                        } else if (statusCode.equals("304", ignoreCase = true)) {
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                getString(R.string.error_heading),
//                                getString(R.string.email_exists),
//                                R.drawable.infoicon,
//                                R.drawable.round
//                            )
//                        } else if (statusCode.equals("306", ignoreCase = true)) {
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                getString(R.string.error_heading),
//                                getString(R.string.invalid_email_first) + mMailEdtText!!.text.toString() + getString(
//                                    R.string.invalid_email_last
//                                ),
//                                R.drawable.exclamationicon,
//                                R.drawable.round
//                            )
//                        }
//                    } else if (responseCode.equals("500", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?,
//                            "Alert",
//                            mContext!!.getString(R.string.common_error),
//                            R.drawable.exclamationicon,
//                            R.drawable.round
//                        )
//                    } else if (responseCode.equals("400", ignoreCase = true)) {
//                        appUtils.getToken(mContext!!)
//                        sendSignUpRequest(URL_PARENT_SIGNUP)
//                    } else if (responseCode.equals("401", ignoreCase = true)) {
//                        appUtils.getToken(mContext!!)
//                        sendSignUpRequest(URL_PARENT_SIGNUP)
//                    } else if (responseCode.equals("402", ignoreCase = true)) {
//                        appUtils.getToken(mContext!!)
//                        sendSignUpRequest(URL_PARENT_SIGNUP)
//                    } else {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?,
//                            "Alert",
//                            mContext!!.getString(R.string.common_error),
//                            R.drawable.exclamationicon,
//                            R.drawable.round
//                        )
//                    }
//
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            }
//
//        })
    }

    private fun loginApiCall() {
        var androidID = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID)
        val call: Call<ResponseBody> = ApiClient.getApiService().loginCall(
            mUserNameEdtTxt!!.text.toString(),
            mPasswordEdtTxt!!.text.toString(),
            "2",
            FirebaseInstanceId.getInstance().id,
            androidID
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        var status = jsonObject.optInt("status")
                        if (status == 100) {
                            val responseArray: JSONObject = jsonObject.optJSONObject("responseArray")
                            val userCode = responseArray.optString("user_code")
                            val token = responseArray.optString("token")
                            PreferenceManager.setUserID(mContext, userCode)
                            PreferenceManager.setAccessToken(mContext, token)
                        } else if (status == 110) {
                            AppUtils.showDialogAlertDismiss(mContext as Activity?,
                                getString(R.string.error_heading),
                                "Incorrect Username or Password",
                                R.drawable.exclamationicon,
                                R.drawable.round)
                        } else if (status == 103) {
                            AppUtils.showDialogAlertDismiss(mContext as Activity?,
                                getString(R.string.error_heading),
                                "Validation Error",
                                R.drawable.exclamationicon,
                                R.drawable.round)
                        } else {
                            AppUtils.showDialogAlertDismiss(mContext as Activity?,
                                "Alert",
                                mContext!!.getString(R.string.common_error),
                                R.drawable.exclamationicon,
                                R.drawable.round)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }


}