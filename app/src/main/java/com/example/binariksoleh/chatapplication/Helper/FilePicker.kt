package com.example.binariksoleh.chatapplication.Helper

import android.app.Activity
import com.example.binariksoleh.chatapplication.R
import droidninja.filepicker.FilePickerBuilder

class FilePicker {

    fun pickFile(activity: Activity) {
        FilePickerBuilder.getInstance().setMaxCount(10)
                .setActivityTheme(R.style.AppTheme)
                .pickFile(activity)

    }


    fun pickPhoto(activity: Activity) {
        FilePickerBuilder.getInstance().setMaxCount(5)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(activity)
    }

}
