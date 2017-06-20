package com.example.binariksoleh.chatapplication.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import com.example.binariksoleh.chatapplication.Helper.FirebaseRoomsHelper
import com.example.binariksoleh.chatapplication.Helper.SharedPreferencesHelper
import com.example.binariksoleh.chatapplication.Model.RoomModel
import com.example.binariksoleh.chatapplication.Presenter.RoomsListPresenter
import com.example.binariksoleh.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_rooms.*
import org.jetbrains.anko.*


class ChatRoomsActivity : AppCompatActivity() {

    private lateinit var chatRoomsAdapter: RoomsListPresenter
    private lateinit var roomsMetadataReference: DatabaseReference
    private lateinit var currentUsersRoomsReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_rooms)
        title = "Chat rooms"

        toast("Welcome back, " + FirebaseAuth.getInstance().currentUser?.email + " !")
        currentUsersRoomsReference = FirebaseDatabase.getInstance().getReference("users/" + SharedPreferencesHelper.getCurrentUserId(this))
        roomsMetadataReference = FirebaseDatabase.getInstance().getReference("room_metadata ")
        chatRoomsAdapter = RoomsListPresenter(ArrayList<RoomModel>())
        val layoutManager = LinearLayoutManager(applicationContext)
        roomsList.adapter = chatRoomsAdapter
        roomsList.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(roomsList.context,
                layoutManager.orientation)
        roomsList.addItemDecoration(dividerItemDecoration)


        getChatRoomsAndPopulateThem()

        add_room_fab.onClick {
            alert {
                var roomName: EditText? = null
                title("Create new room")
                customView {
                    relativeLayout {
                        roomName = editText {
                            hint = "Room name"
                        }

                    }
                }

                val positivebutton = positiveButton("OK") {
                    val newRoom = RoomModel("DEF_ID", System.currentTimeMillis(), FirebaseAuth.getInstance().currentUser?.email!!, roomName?.text.toString())
                    roomsMetadataReference.push().setValue(newRoom)
                }

                val negativeButton = negativeButton("Cancel") {
                    dismiss()
                }


            }.show()
        }


    }

    /**
     * Is called when we enter the Activity.
     * Grabs rooms from database and populates
     * them with RecyclerView
     */
    fun getChatRoomsAndPopulateThem() {
        roomsMetadataReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("Cancelled")
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                chatRoomsAdapter.addRoom(FirebaseRoomsHelper.getRoomFromDataSnapshot(p0!!), chatRoomsAdapter.itemCount)
                roomsList.smoothScrollToPosition(chatRoomsAdapter.itemCount)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                chatRoomsAdapter.deleteMessage(FirebaseRoomsHelper.getRoomPosition(p0!!, chatRoomsAdapter))
            }

        })
    }
}
