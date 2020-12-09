package com.esprit.jobhunter.CompanyFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Adapters.ListoffersInternshipAdapter
import com.esprit.jobhunter.Entity.Internship
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ListinternshipsFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var insternshipList: ArrayList<Internship>? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    var user: User? = null
    var queue: RequestQueue? = null
    var show: Button? = null

    //---
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_listoffers, container, false)
        //------------------------
        queue = Volley.newRequestQueue(activity!!.applicationContext)


        recyclerView = view.findViewById<View>(R.id.offers_job_list) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView!!.layoutManager = layoutManager
        jsonArray

        //------------------------
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return view
    }

    //-----------------------------------------
    //-----------------------------------------


    private val jsonArray: Unit
        private get() {
            val fromLogin = activity!!.intent
            user = fromLogin.getSerializableExtra("connectedUser") as User
            val url = GlobalParams.url + "/internship/" + user!!.id
            insternshipList = ArrayList()
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
                                    internship.educ_req = jsonObject.getString("educ_req")
                                    internship.duration = jsonObject.getString("duration")
                                    internship.user_id = jsonObject.getString("user_id")
                                    internship.skills = jsonObject.getString("skills")

                                    //-----------------------------------------
                                    //-----------------------------------------
                                    insternshipList!!.add(internship)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            adapter = ListoffersInternshipAdapter(activity!!, insternshipList!!)
                            adapter!!.notifyDataSetChanged()
                            recyclerView!!.adapter = adapter
                            adapter!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        println("MYYYLIST$insternshipList")
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }
}