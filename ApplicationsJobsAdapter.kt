package com.esprit.jobhunter.RecyclerViewsAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.esprit.jobhunter.Entity.ApplicationJob
import com.esprit.jobhunter.MainActivity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.squareup.picasso.Picasso
import java.util.*

class ApplicationsJobsAdapter(private val context: Context, list: ArrayList<ApplicationJob>) : RecyclerView.Adapter<ApplicationsJobsAdapter.ViewHolder>() {
    private val list: List<ApplicationJob>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.offer_job_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = list[position]
        val x = position
        holder.textLabel.text = job.label
        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        holder.textCompanyName.text = job.name_company.toString()

        if (job.picture_company!!.length > 60) {
            Picasso.with(context as MainActivity).load(job.picture_company).into(holder.imgCompany)
        } else {
            Picasso.with(context as MainActivity).load(GlobalParams.ressourceUrl + "/" + job.picture_company).into(holder.imgCompany)
        }
        holder.layout.setOnClickListener {
        }
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

    init {
        this.list = list
    }
}