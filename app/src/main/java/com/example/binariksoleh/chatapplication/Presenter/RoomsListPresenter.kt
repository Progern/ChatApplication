package com.example.binariksoleh.chatapplication.Presenter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.binariksoleh.chatapplication.Model.RoomModel
import com.example.binariksoleh.chatapplication.R
import kotlinx.android.synthetic.main.list_room_item.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onLongClick
import java.text.SimpleDateFormat
import java.util.*

class RoomsListPresenter(val roomsList: ArrayList<RoomModel>) : RecyclerView.Adapter<RoomsListPresenter.RoomsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RoomsViewHolder {

        var view: View = LayoutInflater.from(parent?.context).inflate(R.layout.list_room_item, parent, false)
        return RoomsViewHolder(view)

    }

    override fun onBindViewHolder(holder: RoomsViewHolder?, position: Int) {
        holder?.bindRoom(roomsList[position])
    }

    override fun getItemCount(): Int {
        return roomsList.size
    }


    fun addRoom(roomModel: RoomModel, position: Int) {
        roomsList.add(position, roomModel)
        notifyItemInserted(position)
    }


    fun deleteMessage(position: Int) {
        roomsList.removeAt(position)
        notifyItemRemoved(position)
    }


    /**
     * @param view - incoming or outcoming message view
     */
    class RoomsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        /**
         * @param roomModel - object containing message information
         */
        fun bindRoom(roomModel: RoomModel) {
            with(roomModel) {
                itemView.room_title.text = roomModel.name
                itemView.room_creation_time.text = SimpleDateFormat.getDateTimeInstance().format(Date(roomModel.createAt))

                itemView.onClick {
                    //TODO: Start Room Activity
                }

                itemView.onLongClick {
                    //TODO: Hide room?
                    true
                }

                //TODO: Handle "Unread messages"
            }
        }
    }


}

