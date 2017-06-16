package com.example.binariksoleh.chatapplication.Activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.binariksoleh.chatapplication.Helper.FilePicker
import com.example.binariksoleh.chatapplication.Model.ChatMessage
import com.example.binariksoleh.chatapplication.R
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.list_item.*
import org.jetbrains.anko.indeterminateProgressDialog
import java.util.*
import droidninja.filepicker.FilePickerConst
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.storageManager
import java.io.File
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: FirebaseListAdapter<ChatMessage>
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var progressDialog: ProgressDialog
    private lateinit var filePicker: FilePicker
    private lateinit var photoPaths: ArrayList<String>
    private lateinit var filePaths: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.binariksoleh.chatapplication.Helper.ActivityHelper.hideStatusBar(this)
        setContentView(R.layout.activity_chat)
        toast("Welcome, " + FirebaseAuth.getInstance().currentUser?.email + " !")
        progressDialog = indeterminateProgressDialog("Loading chat…")

        filePicker = FilePicker()
        firebaseStorage = FirebaseStorage.getInstance()

        displayChatMessages()


        send_message.onClick {
            FirebaseDatabase.getInstance().reference.push().setValue(ChatMessage(messageField.text.toString(), FirebaseAuth.getInstance().currentUser?.email.toString()))
            messageField.text.clear()
        }

        attach_file.onClick {
            filePicker.pickFile(this)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                photoPaths = ArrayList()
                photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                for (string in photoPaths) {
                    val fileUri = Uri.fromFile(File(string))
                    val uploadTask = firebaseStorage.reference.child(FirebaseAuth.getInstance().currentUser?.email + "/" + fileUri).putFile(fileUri)
                    uploadTask.addOnFailureListener {
                        Log.d("MY_LOG", "Failed to load photo")
                    }

                    uploadTask.addOnSuccessListener {
                        Log.d("MY_LOG", "Succeeded to load photo")
                    }
                }
            }
            FilePickerConst.REQUEST_CODE_DOC -> if (resultCode == Activity.RESULT_OK && data != null) {
                filePaths = ArrayList()
                filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS))
                for (string in filePaths) {
                    val fileUri = Uri.fromFile(File(string))
                    val uploadTask = firebaseStorage.reference.child(FirebaseAuth.getInstance().currentUser?.email + "/" + fileUri).putFile(fileUri)
                    uploadTask.addOnFailureListener {
                        Log.d("MY_LOG", "Failed to load file")
                    }

                    uploadTask.addOnSuccessListener {
                        Log.d("MY_LOG", "Succeeded to load file")
                    }
                }

            }
        }

    }

    private fun displayChatMessages() {
        progressDialog.show()
        adapter = object : FirebaseListAdapter<ChatMessage>(this, ChatMessage::class.java, R.layout.list_item, FirebaseDatabase.getInstance().reference) {


            override fun populateView(v: View, model: ChatMessage, position: Int) {

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
        progressDialog.dismiss()
    }
}
