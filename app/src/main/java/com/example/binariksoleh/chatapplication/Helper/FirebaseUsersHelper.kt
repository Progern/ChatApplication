package com.example.binariksoleh.chatapplication.Helper

import com.example.binariksoleh.chatapplication.Model.UserModel
import com.google.firebase.database.DataSnapshot

class FirebaseUsersHelper {
    companion object {
        fun getUserDataFromSnapshot(dataSnapshot: DataSnapshot): UserModel {
            val child = dataSnapshot.value as HashMap<*, *>?
            val id = child?.get("id")
            val name = child?.get("name")
            val rooms = child?.get("rooms")

            return UserModel()

        }
    }
}