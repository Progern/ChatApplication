package com.example.binariksoleh.chatapplication.Helper

import com.example.binariksoleh.chatapplication.Model.RoomModel
import com.example.binariksoleh.chatapplication.Presenter.RoomsListPresenter
import com.google.firebase.database.DataSnapshot

class FirebaseRoomsHelper {
    companion object {


        /**
         * @param dataSnapshot - piece of data that holds room in
         * HashMap
         * @return - MessageModel object from provided dataSnapshot
         */
        fun getRoomFromDataSnapshot(dataSnapshot: DataSnapshot): RoomModel {
            val child = dataSnapshot.value as HashMap<*, *>?
            val roomId = dataSnapshot.key
            val createAt = child?.get("createAt") as Long
            val createdBy = child.get("createdBy") as String
            val name = child.get("name") as String

            return RoomModel(roomId, createAt, createdBy, name)

        }

        fun getRoomPosition(dataSnapshot: DataSnapshot, roomsListPresenter: RoomsListPresenter): Int {
            return roomsListPresenter.roomsList.firstOrNull { it.name == dataSnapshot.key }
                    ?.let { roomsListPresenter.roomsList.indexOf(it) }
                    ?: 0
        }
    }
}