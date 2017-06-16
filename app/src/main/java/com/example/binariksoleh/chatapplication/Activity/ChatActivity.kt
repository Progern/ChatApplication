package com.example.binariksoleh.chatapplication.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.binariksoleh.chatapplication.Model.ChatMessage

import com.example.binariksoleh.chatapplication.R
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import android.widget.TextView
import com.firebase.ui.auth.ui.ActivityHelper
import java.util.*





class ChatActivity : AppCompatActivity() {

    private lateinit var adapter : FirebaseListAdapter<ChatMessage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.binariksoleh.chatapplication.Helper.ActivityHelper.hideStatusBar(this)
        setContentView(R.layout.activity_chat)
        toast("Welcome, " + FirebaseAuth.getInstance().currentUser?.email + " !")

        displayChatMessages()

        send_message.onClick {
            FirebaseDatabase.getInstance().reference.push().setValue(ChatMessage(messageField.text.toString(),  FirebaseAuth.getInstance().currentUser?.email.toString()))
            messageField.text.clear()
        }
    }

    private fun displayChatMessages() {
        adapter = object : FirebaseListAdapter<ChatMessage>(this, ChatMessage::class.java, R.layout.list_item, FirebaseDatabase.getInstance().reference) {


            override fun populateView(v: View, model: ChatMessage, position: Int) {

                //Get references to the views of list_item.xml
                val messageText: TextView = v.findViewById(R.id.message_text) as TextView
                val messageUser: TextView = v.findViewById(R.id.message_user) as TextView
                val messageTime: TextView = v.findViewById(R.id.message_time) as TextView
                val dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)

                messageText.text = model.text
                messageUser.text = model.user
                messageTime.text = dateFormat.format(Date(model.time))
            }
        }

        messagesList.adapter = adapter


    }
}
