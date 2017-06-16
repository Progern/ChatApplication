package com.example.binariksoleh.chatapplication

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.binariksoleh.chatapplication.Helper.ActivityHelper
import com.example.binariksoleh.chatapplication.Helper.AnimationHelper
import kotlinx.android.synthetic.main.activity_login_or_sign_up.*

class LoginOrSignUpActivity : AppCompatActivity() {

    private var actionFlag : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHelper.hideStatusBar(this)
        setContentView(R.layout.activity_login_or_sign_up)
        signInText.setTextColor(Color.GRAY)

        signUpText.setOnClickListener {
            actionFlag = true
            AnimationHelper.changeImageWithFadeAnimation(this, topImage, actionFlag)
            submitButton.setText(R.string.sign_up)
            signInText.setTextColor(Color.GRAY)
            signUpText.setTextColor(Color.WHITE)
            AnimationHelper.changeTextWithAnimation(submitButton, actionFlag)

        }

        signInText.setOnClickListener {
            actionFlag = false
            AnimationHelper.changeImageWithFadeAnimation(this, topImage, actionFlag)
            submitButton.setText(R.string.sign_in)
            signInText.setTextColor(Color.WHITE)
            signUpText.setTextColor(Color.GRAY)
            AnimationHelper.changeTextWithAnimation(submitButton, actionFlag)
        }
    }
}
