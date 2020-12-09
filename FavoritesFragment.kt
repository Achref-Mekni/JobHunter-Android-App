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
import com.esprit.jobhunter.Entity.Job
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.FavoritesAdapter
import com.esprit.jobhunter.SQLite.DatabaseHelper
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var jobList: ArrayList<Job>? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    var progressBar1: ProgressBar? = null
    private var db: DatabaseHelper? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        //------------------------
        jobList = ArrayList()
        progressBar1 = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        recyclerView = view.findViewById<View>(R.id.favorites_list) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView!!.layoutManager = layoutManager

        //------------------------
        progressBar1!!.visibility = View.VISIBLE
        db = DatabaseHelper(activity)
        if (db!!.jobsCount != 0) {
            jobList = db!!.allJobs
            adapter = FavoritesAdapter(activity!!, jobList!!)
            adapter!!.notifyDataSetChanged()
            recyclerView!!.adapter = adapter
            adapter!!.notifyDataSetChanged()
            progressBar1!!.visibility = View.GONE
        } else if (db!!.jobsCount == 0) {
            progressBar1!!.visibility = View.GONE
            Toast.makeText(activity!!.applicationContext, "No Favorites", Toast.LENGTH_LONG)
        }
        //------------------------
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return view
    }

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}