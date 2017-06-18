package com.example.binariksoleh.chatapplication.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.binariksoleh.chatapplication.Helper.FilePicker
import com.example.binariksoleh.chatapplication.Helper.FirebaseHelper
import com.example.binariksoleh.chatapplication.Model.ChatMessage
import com.example.binariksoleh.chatapplication.R
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: FirebaseListAdapter<ChatMessage>
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var progressDialog: ProgressDialog
    private lateinit var filePicker: FilePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.binariksoleh.chatapplication.Helper.ActivityHelper.hideStatusBar(this)
        setContentView(R.layout.activity_chat)
        toast("Welcome, " + FirebaseAuth.getInstance().currentUser?.email + " !")
        progressDialog = indeterminateProgressDialog("Loading chat…")
        progressDialog.show()

        filePicker = FilePicker()
        firebaseStorage = FirebaseStorage.getInstance()

        getChatMessagesFromFirebaseAndPopulateThem()

        send_message.onClick {
            if (messageField.text.isEmpty()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .playOn(messageField)
            } else {
                FirebaseDatabase.getInstance().reference.push().setValue(ChatMessage(messageField.text.toString(), FirebaseAuth.getInstance().currentUser?.email.toString()))
                messageField.text.clear()
            }
        }

        attach_file.onClick {
            filePicker.pickFile(this)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                val photoPaths = ArrayList<String>()
                photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                FirebaseHelper.loadPhotos(this, firebaseStorage, photoPaths)

            }
            FilePickerConst.REQUEST_CODE_DOC -> if (resultCode == Activity.RESULT_OK && data != null) {
                val filePaths = ArrayList<String>()
                filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS))
                FirebaseHelper.loadFiles(this, firebaseStorage, filePaths)
            }
        }

    }

    private fun getChatMessagesFromFirebaseAndPopulateThem() {
        progressDialog.show()
        adapter = object : FirebaseListAdapter<ChatMessage>(this, ChatMessage::class.java, R.layout.chat_message_outcoming, FirebaseDatabase.getInstance().reference) {

            override fun populateView(v: View, model: ChatMessage, position: Int) {
                //TODO: Write custom adapter

                val messageText: TextView = v.findViewById(R.id.message_text) as TextView
                val messageUser: TextView = v.findViewById(R.id.message_user) as TextView
                val messageTime: TextView = v.findViewById(R.id.message_time) as TextView
                val dateFormat = SimpleDateFormat.getDateTimeInstance()

                messageText.text = model.text
                messageUser.text = model.user
                messageTime.text = dateFormat.format(Date(model.time))


            }
        }
        messagesList.adapter = adapter
        progressDialog.dismiss()
    }

}
