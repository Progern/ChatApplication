package com.example.binariksoleh.chatapplication.Helper

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import com.example.binariksoleh.chatapplication.Manifest

class PermissionsHelper {
    companion object {

        @TargetApi(Build.VERSION_CODES.M)
        fun readExternalStoragePermission(activity: Activity) {
            if (!checkReadExternalStoragePermission(activity)) {
                grantReadExternalStoragePermission(activity)
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        fun writeExternalStoragePermission(activity: Activity) {
            if (!checkWriteExternalStoragePermission(activity)) {
                grantWriteExternalStoragePermission(activity)
            }
        }


        @TargetApi(Build.VERSION_CODES.M)
        private fun checkReadExternalStoragePermission(activity: Activity): Boolean {
            if (activity.applicationContext.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }

        @TargetApi(Build.VERSION_CODES.M)
        private fun checkWriteExternalStoragePermission(activity: Activity): Boolean {
            if (activity.applicationContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }

        @TargetApi(Build.VERSION_CODES.M)
        private fun grantReadExternalStoragePermission(activity: Activity) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }

        @TargetApi(Build.VERSION_CODES.M)
        private fun grantWriteExternalStoragePermission(activity: Activity) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }
}
