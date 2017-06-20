package com.example.binariksoleh.chatapplication.Model

data class RoomModel(val id: Long, val createAt: Long, val createdBy: String, val name: String) {

    /**
     * Default constructor for Firebase
     */
    constructor() : this(0, System.currentTimeMillis(), "testuser@test.com", "Test room #1")
}
