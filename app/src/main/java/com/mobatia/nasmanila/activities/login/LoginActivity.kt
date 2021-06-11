package com.mobatia.nasmanila.activities.login

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.constants.manager.PreferenceManager

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
}