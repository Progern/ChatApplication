package com.example.binariksoleh.chatapplication.Helper

import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager

class ActivityHelper {
    companion object {

        fun hideStatusBar(activity: AppCompatActivity) {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
            activity.supportActionBar?.hide()
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}
