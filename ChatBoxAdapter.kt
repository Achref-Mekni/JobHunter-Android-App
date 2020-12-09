package com.esprit.jobhunter.RecyclerViewsAdapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esprit.jobhunter.Entity.Message
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.ChatBoxAdapter.MyViewHolder

class ChatBoxAdapter(private val MessageList: List<Message>, private val MessageList2: List<Message>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun getItemCount(): Int {
        return MessageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //binding the data from our ArrayList of object to the item.xml using the viewholder
        val m = MessageList[position]
        if (m.test == 0) {
            holder.nickname.text = m.sender_name
            holder.message.text = m.content
        } else if (m.test == 1) {
            holder.nickname.text = m.receiver_name
            holder.message.text = m.content
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nickname: TextView
        var nickname1: TextView? = null
        var message: TextView
        var message1: TextView? = null

        init {
            nickname = view.findViewById<View>(R.id.m_name) as TextView
            message = view.findViewById<View>(R.id.message) as TextView

        }
    }

}