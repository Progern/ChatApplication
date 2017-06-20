package com.example.binariksoleh.chatapplication.Helper

import android.content.Context
import com.example.binariksoleh.chatapplication.Config.AppConstants

class SharedPreferencesHelper {
    companion object {

        fun setCurrentUserId(userId: String, context: Context) {
            context.getSharedPreferences("com.example.binariksoleh.chatapplication", Context.MODE_PRIVATE).edit().putString(AppConstants.CURRENT_USER_PREFERENCES_ID, userId).commit()
        }

        fun getCurrentUserId(context: Context): String {
            return context.getSharedPreferences("com.example.binariksoleh.chatapplication", Context.MODE_PRIVATE).getString("currentUser", AppConstants.DEFAULT_USER)
        }
    }
}