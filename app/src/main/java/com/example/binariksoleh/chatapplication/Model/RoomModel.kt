package com.example.binariksoleh.chatapplication.Model

data class RoomModel(val id: String, val createAt: Long, val createdBy: String, val name: String) {

    /**
     * Default constructor for Firebase
     */
    constructor() : this("DEF_ID", System.currentTimeMillis(), "testuser@test.com", "Test room #1")
}
