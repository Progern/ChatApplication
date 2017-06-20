package com.example.binariksoleh.chatapplication.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.binariksoleh.chatapplication.Helper.FilePicker
import com.example.binariksoleh.chatapplication.Helper.FirebaseChatHelper
import com.example.binariksoleh.chatapplication.Model.MessageModel
import com.example.binariksoleh.chatapplication.Presenter.ChatPresenter
import com.example.binariksoleh.chatapplication.R
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast


class ChatActivity : AppCompatActivity() {

    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var chatMessagesAdapter: ChatPresenter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var filePicker: FilePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //TODO: Get chat room from preferences
        title = ""


        filePicker = FilePicker()
        firebaseStorage = FirebaseStorage.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        chatMessagesAdapter = ChatPresenter(ArrayList<MessageModel>())
        val layoutManager = LinearLayoutManager(applicationContext)
        messagesList.adapter = chatMessagesAdapter
        messagesList.layoutManager = layoutManager


        getChatMessagesAndPopulateThem()

        send_message.onClick {
            if (messageField.text.isEmpty()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .playOn(messageField)
            } else {
                //FirebaseDatabase.getInstance().reference.push().setValue(MessageModel("", messageField.text.toString(), FirebaseAuth.getInstance().currentUser?.email.toString(), Calendar.getInstance().timeInMillis, AppConstants.OUTCOMING_MESSAGE, false))
                messageField.text.clear()
            }
        }

        attach_file.onClick {
            filePicker.pickPhoto(this)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                val photoPaths = ArrayList<String>()
                photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                FirebaseChatHelper.loadPhotos(this, firebaseStorage, photoPaths, chatMessagesAdapter)
                messagesList.smoothScrollToPosition(chatMessagesAdapter.itemCount)

            }
            FilePickerConst.REQUEST_CODE_DOC -> if (resultCode == Activity.RESULT_OK && data != null) {
                val filePaths = ArrayList<String>()
                filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS))
                FirebaseChatHelper.loadFiles(this, firebaseStorage, filePaths, chatMessagesAdapter)
                messagesList.smoothScrollToPosition(chatMessagesAdapter.itemCount)
            }
        }

    }

    /**
     * Is called when we enter the Activity.
     * Grabs messages from database and populates
     * them with RecyclerView
     */
    private fun getChatMessagesAndPopulateThem() {
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("Cancelled")
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                chatMessagesAdapter.addMessage(FirebaseChatHelper.getMessageFromDataSnapshot(p0!!), chatMessagesAdapter.itemCount)
                messagesList.smoothScrollToPosition(chatMessagesAdapter.itemCount)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                chatMessagesAdapter.deleteMessage(FirebaseChatHelper.getItemPosition(p0!!, chatMessagesAdapter))
            }

        })
    }


}
