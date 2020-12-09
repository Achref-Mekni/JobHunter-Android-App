package com.esprit.jobhunter.OffersFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.Internship
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.OffersInternshipsAdapter
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class OffersInternshipsFragment : Fragment() {
    //---
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var internshipList: ArrayList<Internship>? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    var progressBar1: ProgressBar? = null
    var url = GlobalParams.url + "/internships"
    var internship: Internship? = null
    var queue: RequestQueue? = null
    var url2 = GlobalParams.url + "/getuser"
    var companyUser: User? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers_internships, container, false)
        //------------------------
        queue = Volley.newRequestQueue(activity!!.applicationContext)

        //jobList = new ArrayList<>();
        progressBar1 = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        recyclerView = view.findViewById<View>(R.id.offers_internship_list) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView!!.layoutManager = layoutManager
        jsonArray
        println("JOBLIST$internshipList")

        //------------------------
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return view
    }

    //-----------------------------------------
    //-----------------------------------------



    private val jsonArray: Unit
        private get() {
            progressBar1!!.visibility = View.VISIBLE
            internshipList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            jsonArray = response.getJSONArray("result")

                            for (i in 0 until jsonArray.length()) {
                                try {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val internship = Internship()
                                    internship.id = jsonObject.getInt("id")
                                    internship.label = jsonObject.getString("label")
                                    internship.description = jsonObject.getString("description")
                                    internship.start_date = jsonObject.getString("start_date")
                                    internship.duration = jsonObject.getString("duration")
                                    internship.educ_req = jsonObject.getString("educ_req")
                                    internship.skills = jsonObject.getString("skills")
                                    internship.user_id = jsonObject.getString("user_id")
                                    internship.company_name = jsonObject.getString("name")
                                    internship.company_pic = jsonObject.getString("picture")

                                    //-----------------------------------------
                                    //-----------------------------------------
                                    internshipList!!.add(internship)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            adapter = OffersInternshipsAdapter(activity!!, internshipList!!)
                            adapter!!.notifyDataSetChanged()
                            recyclerView!!.adapter = adapter
                            adapter!!.notifyDataSetChanged()
                            progressBar1!!.visibility = View.GONE
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }
}