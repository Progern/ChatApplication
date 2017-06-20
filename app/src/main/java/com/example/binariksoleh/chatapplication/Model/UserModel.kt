package com.example.binariksoleh.chatapplication.Model

data class UserModel(val id: String, val name: String, val rooms: ArrayList<String>) {

    /**
     * Default constructor for Firebase
     */
    constructor() : this("DEF_ID", "DEF_NAME", ArrayList<String>())

}