package com.esprit.jobhunter.RecyclerViewsAdapters.ProfileAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esprit.jobhunter.Entity.Education
import com.esprit.jobhunter.R
import java.util.*

class EducationAdapter(private val context: Context, list: ArrayList<Education>) : RecyclerView.Adapter<EducationAdapter.ViewHolder>() {
    private val list: List<Education>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.education_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val education = list[position]
        holder.textInstitution.text = education.inst_name
        holder.textDegree.text = education.degree
        holder.textDomain.text = education.domain
        holder.textStartDate.text = education.start_date
        holder.textEndDate.text = education.end_date
        holder.textResult.text = education.result
        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        //holder.textCompanyName.setText(String.valueOf(job.getCompany().getName()));
        //holder.imgCompany.setImageResource(String.valueOf(job.getYear()));
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textInstitution: TextView
        var textDegree: TextView
        var textDomain: TextView
        var textStartDate: TextView
        var textEndDate: TextView
        var textResult: TextView

        init {
            textInstitution = itemView.findViewById(R.id.inst_name)
            textDegree = itemView.findViewById(R.id.degree)
            textDomain = itemView.findViewById(R.id.domain)
            textStartDate = itemView.findViewById(R.id.start_date)
            textEndDate = itemView.findViewById(R.id.end_date)
            textResult = itemView.findViewById(R.id.result)
        }
    }

    init {
        this.list = list
    }
}