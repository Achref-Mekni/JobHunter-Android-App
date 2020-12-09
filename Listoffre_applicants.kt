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
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Adapters.ListApplicantsAdapter
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.Entity.User_Job
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Listoffre_applicants : Fragment() {

    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var users: ArrayList<User>? = null
    private var usersjob: ArrayList<User_Job>? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    var ids = 0
    var count: TextView? = null
    var url: String? = null
    var size = 0
    var queue: RequestQueue? = null
    var show: Button? = null

    //---
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_listoffre_applicants, container, false)
        //------------------------
        queue = Volley.newRequestQueue(activity!!.applicationContext)

        recyclerView = view.findViewById<View>(R.id.offers_applicants_list) as RecyclerView
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
            if (arguments!!["id"] != null) {
                ids = arguments!!["id"] as Int
                url = GlobalParams.url + "/job/applicants/" + ids
            } else {
                ids = arguments!!["id1"] as Int
                url = GlobalParams.url + "/internship/applicants/" + ids
            }
            println(url)
            users = ArrayList()
            usersjob = ArrayList()
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

                                    val user = User()
                                    val userjob = User_Job()
                                    user.id = jsonObject.getInt("id")
                                    user.name = jsonObject.getString("name")
                                    user.last_name = jsonObject.getString("last_name")
                                    user.nationality = jsonObject.getString("nationality")
                                    user.picture = jsonObject.getString("picture")
                                    user.email = jsonObject.getString("email")
                                    user.adress = jsonObject.getString("adress")
                                    user.tel1 = jsonObject.getString("tel1")
                                    user.tel2 = jsonObject.getString("tel2")
                                    user.description = jsonObject.getString("description")
                                    userjob.creation_date = "creation_date"
                                    //-----------------------------------------
                                    //-----------------------------------------
                                    users!!.add(user)
                                    usersjob!!.add(userjob)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            adapter = ListApplicantsAdapter(activity!!, users!!)
                            adapter!!.notifyDataSetChanged()
                            recyclerView!!.adapter = adapter
                            adapter!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        println("MYYYLIST$users")
                        count = view!!.findViewById<View>(R.id.count) as TextView
                        count!!.text = users!!.size.toString()
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue!!.add(jsonArrayRequest)
        }
}