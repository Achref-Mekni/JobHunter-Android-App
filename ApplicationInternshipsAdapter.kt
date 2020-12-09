package com.esprit.jobhunter.RecyclerViewsAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.esprit.jobhunter.Entity.ApplicationInternship
import com.esprit.jobhunter.MainActivity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.squareup.picasso.Picasso
import java.util.*

class ApplicationInternshipsAdapter(private val context: Context, list: ArrayList<ApplicationInternship>) : RecyclerView.Adapter<ApplicationInternshipsAdapter.ViewHolder>() {
    private var lastPosition = -1
    private val list: List<ApplicationInternship>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.offer_job_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val internship = list[position]
        val x = position
        holder.textLabel.text = internship.label

        holder.textCompanyName.text = internship.name_company.toString()

        if (internship.picture_company!!.length > 60) {
            Picasso.with(context as MainActivity).load(internship.picture_company).into(holder.imgCompany)
        } else {
            Picasso.with(context as MainActivity).load(GlobalParams.ressourceUrl + "/" + internship.picture_company).into(holder.imgCompany)
        }
        holder.layout.setOnClickListener {
        }
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLabel: TextView
        var textCompanyName: TextView
        var imgCompany: ImageView
        var layout: RelativeLayout

        init {
            textLabel = itemView.findViewById(R.id.job_label)
            textCompanyName = itemView.findViewById(R.id.company_name)
            imgCompany = itemView.findViewById(R.id.job_image)
            layout = itemView.findViewById(R.id.ojli_layout)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    init {
        this.list = list
    }
}