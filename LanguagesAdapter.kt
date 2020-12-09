package com.esprit.jobhunter.RecyclerViewsAdapters.ProfileAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esprit.jobhunter.Entity.Language
import com.esprit.jobhunter.R
import java.util.*

class LanguagesAdapter(private val context: Context, list: ArrayList<Language>) : RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {
    private val list: List<Language>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.languages_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lan = list[position]
        holder.textLabel.text = lan.label
        holder.textLevel.text = lan.level

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLabel: TextView
        var textLevel: TextView

        init {
            textLabel = itemView.findViewById(R.id.lang_label)
            textLevel = itemView.findViewById(R.id.lang_level)
        }
    }

    init {
        this.list = list
    }
}