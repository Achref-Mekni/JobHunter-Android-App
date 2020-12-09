package com.esprit.jobhunter.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.esprit.jobhunter.Entity.Offer
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class SearchAdapter(context: Context, resource: Int, private val OfferList: List<Offer>) : ArrayAdapter<Offer?>(context, resource, OfferList) {
    private val tempList: List<Offer>
    private val suggestionList: MutableList<Offer>
    init {
        tempList = ArrayList(OfferList)
        suggestionList = ArrayList()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.search_row, parent, false)
        val offer_label = convertView!!.findViewById<TextView>(R.id.job_label)
        val cmp_name = convertView.findViewById<TextView>(R.id.company_name)
        val cmp_pic: CircleImageView = convertView.findViewById(R.id.cmp_pic)
        val offer = OfferList[position]
        offer_label.text = offer.label
        cmp_name.text = offer.cmp_name


        if (offer.cmp_pic!!.length > 60) {
            Picasso.with(convertView.context).load(offer.cmp_pic).into(cmp_pic)
        } else {
            Picasso.with(convertView.context).load(GlobalParams.ressourceUrl + "/" + offer.cmp_pic).into(cmp_pic)
        }
        return convertView
    }

    override fun getFilter(): Filter {
        return offerFilter
    }

    var offerFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String? {
            val offer = resultValue as Offer
            return offer.label
        }

        override fun performFiltering(constraint: CharSequence): FilterResults {
            var constraint: CharSequence? = constraint
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                suggestionList.clear()
                constraint = constraint.toString().trim { it <= ' ' }.toLowerCase()
                for (offer in tempList) {
                    if (offer.label!!.toLowerCase().contains(constraint) || offer.cmp_name!!.toLowerCase().contains(constraint)) {
                        suggestionList.add(offer)
                    }
                }
                filterResults.count = suggestionList.size
                filterResults.values = suggestionList
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            val uList = results.values as ArrayList<Offer>
            if (results != null && results.count > 0) {
                clear()
                for (u in uList) {
                    add(u)
                    notifyDataSetChanged()
                }
            }
        }
    }


}