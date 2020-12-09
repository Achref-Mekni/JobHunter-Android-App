package com.esprit.jobhunter.RecyclerViewsAdapters.ProfileAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esprit.jobhunter.Entity.Volunteer
import com.esprit.jobhunter.R
import java.util.*

class VolunteerAdapter(private val context: Context, list: ArrayList<Volunteer>) : RecyclerView.Adapter<VolunteerAdapter.ViewHolder>() {
    private val list: List<Volunteer>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.volunteer_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vol = list[position]
        holder.textLabel.text = vol.organisation
        holder.textRole.text = vol.role
        holder.textStartDate.text = vol.start_date
        if (vol.still_going == 0) holder.textEndDate.text = vol.end_date else holder.textEndDate.text = "Today"

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLabel: TextView
        var textRole: TextView
        var textStartDate: TextView
        var textEndDate: TextView

        init {
            textLabel = itemView.findViewById(R.id.vol_org_name)
            textRole = itemView.findViewById(R.id.vol_role)
            textStartDate = itemView.findViewById(R.id.start_date)
            textEndDate = itemView.findViewById(R.id.end_date)
        }
    }

    init {
        this.list = list
    }
}