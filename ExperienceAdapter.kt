package com.esprit.jobhunter.RecyclerViewsAdapters.ProfileAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esprit.jobhunter.Entity.Experience
import com.esprit.jobhunter.R
import java.util.*

class ExperienceAdapter(private val context: Context, list: ArrayList<Experience>) : RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {
    private val list: List<Experience>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.experience_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val experience = list[position]
        holder.textLabel.text = experience.label
        holder.textEstab.text = experience.establishmentName
        holder.textStartDate.text = experience.start_date
        holder.textDesc.text = experience.description
        if (experience.still_going == 0) holder.textEndDate.text = experience.end_date else holder.textDesc.text = "Today"

        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        //holder.textCompanyName.setText(String.valueOf(job.getCompany().getName()));
        //holder.imgCompany.setImageResource(String.valueOf(job.getYear()));
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLabel: TextView
        var textEstab: TextView
        var textStartDate: TextView
        var textEndDate: TextView
        var textDesc: TextView

        init {
            textLabel = itemView.findViewById(R.id.ex_label)
            textEstab = itemView.findViewById(R.id.ex_company)
            textStartDate = itemView.findViewById(R.id.ex_start_date)
            textEndDate = itemView.findViewById(R.id.ex_end_date)
            textDesc = itemView.findViewById(R.id.ex_desc)
        }
    }

    init {
        this.list = list
    }
}