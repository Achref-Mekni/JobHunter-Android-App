package com.esprit.jobhunter.RecyclerViewsAdapters.ProfileAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esprit.jobhunter.Entity.Certification
import com.esprit.jobhunter.R
import java.util.*

class CertificationsAdapter(private val context: Context, list: ArrayList<Certification>) : RecyclerView.Adapter<CertificationsAdapter.ViewHolder>() {
    private val list: List<Certification>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.certifications_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cert = list[position]
        holder.textLabel.text = cert.label
        holder.textAuth.text = cert.cert_authority
        holder.textStartDate.text = cert.cert_date
        if (cert.if_expire == 0) {
            holder.textEndDate.text = cert.expire_date
        } else {
            holder.textEndDate.text = "-"
        }
        holder.textLicence.text = cert.licence_num
        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        //holder.textCompanyName.setText(String.valueOf(job.getCompany().getName()));
        //holder.imgCompany.setImageResource(String.valueOf(job.getYear()));
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLabel: TextView
        var textAuth: TextView
        var textStartDate: TextView
        var textEndDate: TextView
        var textLicence: TextView

        init {
            textLabel = itemView.findViewById(R.id.cert_label)
            textAuth = itemView.findViewById(R.id.cert_auth)
            textStartDate = itemView.findViewById(R.id.cert_obtained_date)
            textEndDate = itemView.findViewById(R.id.cert_expire_date)
            textLicence = itemView.findViewById(R.id.cert_licence)
        }
    }

    init {
        this.list = list
    }
}