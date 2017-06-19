package com.example.binariksoleh.chatapplication.Helper

import android.content.Context
import android.net.Uri
import com.example.binariksoleh.chatapplication.Config.AppConstants
import com.example.binariksoleh.chatapplication.Model.MessageModel
import com.example.binariksoleh.chatapplication.Presenter.ChatPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.toast
import java.io.File
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.firstOrNull
import kotlin.collections.get
import kotlin.collections.mapTo


class FirebaseHelper {
    companion object {

        /**
         * @param context - to show toasts
         * @param firebaseStorage - FirebaseStorage object reference
         * where to load photos
         * @param photoPaths - array of photos to be loaded into storage
         */

        fun loadPhotos(context: Context, firebaseStorage: FirebaseStorage, photoPaths: ArrayList<String>, chatPresenter: ChatPresenter) {
            val filesCount = photoPaths.size
            var loadedFiles = 0
            for (string in photoPaths) {
                val fileUri = Uri.fromFile(File(string))
                val uploadTask = firebaseStorage.reference.child(FirebaseAuth.getInstance().currentUser?.email + "/" + fileUri).putFile(fileUri)
                uploadTask.addOnFailureListener {
                    context.toast("Photo loading failed.")
                }

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    loadedFiles++
                    context.toast("Loaded $loadedFiles of $filesCount photos.")
                    val downloadUri = taskSnapshot.metadata?.downloadUrl
                    chatPresenter.sendMessage(downloadUri.toString())
                }
            }
        }

        /**
         * @param context - to show toasts
         * @param firebaseStorage - FirebaseStorage object reference
         * where to load files
         * @param filePaths - array of files to be loaded into storage
         */

        fun loadFiles(context: Context, firebaseStorage: FirebaseStorage, filePaths: ArrayList<String>, chatPresenter: ChatPresenter) {
            val filesCount = filePaths.size
            var loadedFiles = 0
            for (string in filePaths) {
                val fileUri = Uri.fromFile(File(string))
                val uploadTask = firebaseStorage.reference.child(FirebaseAuth.getInstance().currentUser?.email + "/" + fileUri).putFile(fileUri)

                uploadTask.addOnFailureListener {
                    context.toast("File loading failed.")
                }

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    loadedFiles++
                    context.toast("Loaded $loadedFiles of $filesCount files")
                    val downloadUri = taskSnapshot.metadata?.downloadUrl
                    chatPresenter.sendMessage(downloadUri.toString())
                }


            }

        }

        /**
         * @param dataSnapshot - holds all messages
         * Encapsulates messages from the $dataSnapshot
         * and @return - list of MessageModel object
         */
        fun getList(dataSnapshot: DataSnapshot): List<MessageModel> {
            val messagesList = ArrayList<MessageModel>()
            dataSnapshot.children.mapTo(messagesList) { getMessageFromDataSnapshot(it) }
            return messagesList
        }

        /**
         * @param dataSnapshot - piece of data that holds message in
         * @return that item's (message's) position in list
         */
        fun getItemPosition(dataSnapshot: DataSnapshot, chatMessagesAdapter: ChatPresenter): Int {
            return chatMessagesAdapter.messagesList
                    .firstOrNull { it.text == dataSnapshot.key }
                    ?.let { chatMessagesAdapter.messagesList.indexOf(it) }
                    ?: 0
        }


        /**
         * @param dataSnapshot - piece of data that holds message in
         * HashMap
         * @return - MessageModel object from provided dataSnapshot
         */
        fun getMessageFromDataSnapshot(dataSnapshot: DataSnapshot): MessageModel {
            val child = dataSnapshot.value as HashMap<*, *>?
            val user = child?.get("user") as String
            val text = child["text"] as String
            val time = child["time"] as Long
            val id = dataSnapshot.key
            return MessageModel(id, text, user, time,
                    if (user == FirebaseAuth.getInstance().currentUser?.email)
                        AppConstants.OUTCOMING_MESSAGE
                    else
                        AppConstants.INCOMING_MESSAGE, false)
        }
    }
}