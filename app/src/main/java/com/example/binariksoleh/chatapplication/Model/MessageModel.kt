package com.example.binariksoleh.chatapplication.Model


import java.util.*

data class ChatMessage(val text: String, val user: String, val time: Long = Calendar.getInstance().timeInMillis) {

    /**
     * Default constructor for Firebase
     */
    constructor() : this("EMPTY_MESSAGE", "USERNAME")
}
