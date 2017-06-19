package com.example.binariksoleh.chatapplication.Presenter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.binariksoleh.chatapplication.Config.AppConstants
import com.example.binariksoleh.chatapplication.Model.MessageModel
import com.example.binariksoleh.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.yarolegovich.lovelydialog.LovelyStandardDialog
import kotlinx.android.synthetic.main.chat_message_incoming.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onLongClick
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*


class ChatPresenter(val messagesList: ArrayList<MessageModel>) : RecyclerView.Adapter<ChatPresenter.ChatMessageViewHolder>() {


    /**
     * @param viewType - due to viewtype we detect whether
     * the message is incoming or outcoming and inflate the
     * appropriate view
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChatMessageViewHolder {

        var view: View = LayoutInflater.from(parent?.context).inflate(R.layout.chat_message_outcoming, parent, false)

        if (viewType == AppConstants.INCOMING_MESSAGE) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.chat_message_incoming, parent, false)

        }
        return ChatMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder?, position: Int) {
        holder?.bindMessage(messagesList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return messagesList[position].isMyMessage
    }

    /**
     * If the message was seen by another user individually
     * then set the message color to dark grey
     * TODO: Add "Seen" text like in Messenger
     */
    override fun onViewRecycled(holder: ChatMessageViewHolder?) {
        super.onViewRecycled(holder)
        holder?.itemView?.message_text?.textColor = Color.DKGRAY
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }


    fun addMessage(messageModel: MessageModel, position: Int) {
        messagesList.add(position, messageModel)
        notifyItemInserted(position)
    }


    fun deleteMessage(position: Int) {
        messagesList.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * @param message - holds URL to be packed into
     * new message and sent to Firebase Database
     */
    fun sendMessage(message: String) {
        val database = FirebaseDatabase.getInstance()
        val messagesRef = database.reference
        val newMessageRef = messagesRef.push()
        newMessageRef.setValue(MessageModel("message_id", message, FirebaseAuth.getInstance().currentUser?.email!!, System.currentTimeMillis(), AppConstants.OUTCOMING_MESSAGE, false))
    }


    /**
     * @param view - incoming or outcoming message view
     */
    class ChatMessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        /**
         * @param messageModel - object containing message information
         */
        fun bindMessage(messageModel: MessageModel) {
            with(messageModel) {

                if (messageModel.text.contains("http")) {
                    val content = SpannableString("Attached file")
                    content.setSpan(UnderlineSpan(), 0, content.length, 0)
                    itemView.message_text.text = content

                } else {
                    itemView.message_text.text = messageModel.text
                }
                itemView.message_user.text = messageModel.user
                itemView.message_time.text = SimpleDateFormat.getDateTimeInstance().format(Date(messageModel.time))

                itemView.onClick {
                    if (messageModel.text.contains("http")) {
                        showExternalLinkDialog(messageModel)
                    }
                }

                itemView.onLongClick {
                    if (messageModel.isSelected) {
                        itemView.backgroundResource = R.color.transparent
                        messageModel.isSelected = false
                    } else {
                        itemView.backgroundResource = R.color.selected_message
                        messageModel.isSelected = true
                    }
                    messageModel.isSelected
                }
            }
        }

        /**
         * @param messageModel - message, that holds URL
         * to be loaded in WebView
         */
        private fun showExternalLinkDialog(messageModel: MessageModel) {
            LovelyStandardDialog(itemView.context)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("External action")
                    .setMessage("Do you want to load link")
                    .setIcon(R.drawable.ic_external_link_white)
                    .setPositiveButton("YES", {

                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(messageModel.text)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        itemView.context.startActivity(i)
                    })
                    .setNegativeButton("NO", {})
                    .show()
        }

    }

}