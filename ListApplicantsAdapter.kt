package com.esprit.jobhunter.Adapters

import android.app.Activity
import android.companion.CompanionDeviceManager
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.Main2Activity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ListApplicantsAdapter(private val context: Context, list: ArrayList<User>) : RecyclerView.Adapter<ListApplicantsAdapter.ViewHolder>() {

    private val list: List<User>
    var activity: Activity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.offer_applicant_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = list[position]

        val name = user.name + " " + user.last_name
        holder.applicant_name.text = name
        holder.applicant_email.text = user.email

        if (user.picture != null) {

            if (user.picture!!.length > 60) {
                Picasso.with(context as Main2Activity).load(user.picture).into(holder.applicant_image)
            } else {
                Picasso.with(context as Main2Activity).load(GlobalParams.ressourceUrl + "/" + user.picture).into(holder.applicant_image)
            }
        }
        if (user.nationality != null) {

            val nat = "https://www.countries-ofthe-world.com/flags-normal/flag-of-" + user.nationality + ".png"

            Picasso.with(context.applicationContext)
                    .load(nat)
                    .into(holder.applicant_natio)
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var applicant_name: TextView
        var applicant_email: TextView
        var applicant_natio: CircleImageView
        var applicant_image: CircleImageView
        var accept: Button?

        init {
            accept = itemView.findViewById(R.id.accept)
            applicant_image = itemView.findViewById(R.id.applicant_image)
            applicant_name = itemView.findViewById(R.id.applicant_name)
            applicant_natio = itemView.findViewById(R.id.applicant_natio)
            applicant_email = itemView.findViewById(R.id.applicant_email)



        }


    }


    init {
        this.list = list
    }
}