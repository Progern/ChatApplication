package com.example.binariksoleh.chatapplication.Model


import com.example.binariksoleh.chatapplication.Config.AppConstants
import java.util.*


public data class MessageModel(val id: String, val text: String, val user: String, val time: Long, val isMyMessage: Int, var isSelected: Boolean) {

    /**
     * Default constructor for Firebase
     */
    constructor() : this("0", "EMPTY_MESSAGE", "USERNAME", Calendar.getInstance().timeInMillis, AppConstants.OUTCOMING_MESSAGE, false)


}
