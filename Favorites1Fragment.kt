package com.esprit.jobhunter.MenuFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.esprit.jobhunter.Entity.Internship
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.Favorites1Adapter
import com.esprit.jobhunter.SQLite.Database1Helper
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Favorites1Fragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var internshipList: ArrayList<Internship>? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    var progressBar1: ProgressBar? = null
    private var db: Database1Helper? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        //------------------------
        internshipList = ArrayList()
        progressBar1 = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        recyclerView = view.findViewById<View>(R.id.favorites_list) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView!!.layoutManager = layoutManager

        //------------------------
        progressBar1!!.visibility = View.VISIBLE
        db = Database1Helper(activity)
        if (db!!.internshipsCount != 0) {
            internshipList = db!!.allInternships
            adapter = Favorites1Adapter(activity!!, internshipList!!)
            adapter!!.notifyDataSetChanged()
            recyclerView!!.adapter = adapter
            adapter!!.notifyDataSetChanged()
            progressBar1!!.visibility = View.GONE
        }
        if (db!!.internshipsCount == 0) {
            progressBar1!!.visibility = View.GONE
            Toast.makeText(activity!!.applicationContext, "No Favorites", Toast.LENGTH_LONG)
        }
        //------------------------
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return view
    }

    companion object {
        fun newInstance(): Favorites1Fragment {
            return Favorites1Fragment()
        }
    }
}