package com.esprit.jobhunter.RecyclerViewsAdapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MainActivity
import com.esprit.jobhunter.MessengerFragments.ChatBoxFragment
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.squareup.picasso.Picasso
import java.util.*

class ContactsAdapter(private val context: Context, list: ArrayList<User>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private val list: List<User>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        println("SSSSSSSSSSSSSSSSS: $user")
        holder.name_user.text = user.name

        if (user.picture!!.length > 60) {
            Picasso.with(context as MainActivity).load(user.picture).into(holder.img_user)
        } else {
            Picasso.with(context as MainActivity).load(GlobalParams.ressourceUrl + "/" + user.picture).into(holder.img_user)
        }
        holder.layout.setOnClickListener {
            val fragment: Fragment = ChatBoxFragment()
            (fragment as ChatBoxFragment).setReceiverUser(list[position])

            context.supportFragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name_user: TextView
        var img_user: ImageView
        var layout: RelativeLayout

        init {
            name_user = itemView.findViewById(R.id.contact_name)
            img_user = itemView.findViewById(R.id.contact_pic)
            layout = itemView.findViewById(R.id.contact_item_layout)
        }
    }

    init {
        this.list = list
    }
}