package com.esprit.jobhunter.ApplicationsFragments

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
import com.esprit.jobhunter.Entity.ApplicationInternship
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MainActivity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.ApplicationInternshipsAdapter
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ApplicationsInternshipsFragment : Fragment() {
    //---
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var applicationInternshipList: ArrayList<ApplicationInternship>? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    var progressBar1: ProgressBar? = null
    var url = GlobalParams.url + "/getintappbyuserid/"
    var job: ApplicationInternship? = null
    var queue: RequestQueue? = null
    private var connectedUser: User? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_applications_internships, container, false)
        //------------------------
        connectedUser = (activity as MainActivity?)!!.connectedUser
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        progressBar1 = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        recyclerView = view.findViewById<View>(R.id.application_internship_list) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView!!.layoutManager = layoutManager
        jsonArray
        setHasOptionsMenu(true)
        return view
    }//-----------------------------------------



    private val jsonArray: Unit
        private get() {
            progressBar1!!.visibility = View.VISIBLE
            applicationInternshipList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url + connectedUser!!.id,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            jsonArray = response.getJSONArray("result")

                            for (i in 0 until jsonArray.length()) {
                                try {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val applicationInternship = ApplicationInternship()
                                    applicationInternship.id_applicant = jsonObject.getInt("id_user")
                                    applicationInternship.id_job = jsonObject.getInt("id_internship")
                                    applicationInternship.creation_date = jsonObject.getString("creation_date")
                                    applicationInternship.label = jsonObject.getString("label")
                                    applicationInternship.description = jsonObject.getString("description")
                                    applicationInternship.start_date = jsonObject.getString("start_date")
                                    applicationInternship.duration = jsonObject.getString("duration")
                                    applicationInternship.educ_req = jsonObject.getString("educ_req")
                                    applicationInternship.id_company = jsonObject.getInt("user_id")
                                    applicationInternship.name_company = jsonObject.getString("name")
                                    applicationInternship.picture_company = jsonObject.getString("picture")
                                    //-----------------------------------------
                                    applicationInternshipList!!.add(applicationInternship)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            adapter = ApplicationInternshipsAdapter(activity!!, applicationInternshipList!!)
                            adapter!!.notifyDataSetChanged()
                            recyclerView!!.adapter = adapter
                            adapter!!.notifyDataSetChanged()
                            progressBar1!!.visibility = View.GONE
                            println("FFFFFFFFFFFFFFFF: $applicationInternshipList")
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