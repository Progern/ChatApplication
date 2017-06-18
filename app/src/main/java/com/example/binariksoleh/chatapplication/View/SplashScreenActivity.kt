package com.example.binariksoleh.chatapplication.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.binariksoleh.chatapplication.Helper.ActivityHelper
import com.example.binariksoleh.chatapplication.Helper.AnimationHelper

import com.example.binariksoleh.chatapplication.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import android.R.raw
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import com.example.binariksoleh.chatapplication.Helper.PermissionsHelper
import org.jetbrains.anko.intentFor


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHelper.hideStatusBar(this)
        setContentView(R.layout.activity_splash_screen)

        splash_screen_image.animation = AnimationHelper.splashScreenFadeInAnimation(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsHelper.readExternalStoragePermission(this)
            PermissionsHelper.writeExternalStoragePermission(this)
        }

        var handler = Handler()
        handler.postDelayed({
            startActivity(intentFor<LoginOrSignUpActivity>())
        }, 5000)
    }
}
