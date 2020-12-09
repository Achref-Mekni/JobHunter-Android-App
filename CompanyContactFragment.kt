package com.esprit.jobhunter.CompanyFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.Main2Activity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.MyUtils.SimpleDividerItemDecoration
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.ContactsCompanyAdapter
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CompanyContactFragment : Fragment() {
    private val btn: Button? = null
    private val nickname: EditText? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var userList: ArrayList<User>? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    var progressBar1: ProgressBar? = null
    var url = GlobalParams.url + "/getapprouvedjobappbycompany/"
    var queue: RequestQueue? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        url += (activity as Main2Activity?)!!.connectedUser!!.id

        //---------------
        queue = Volley.newRequestQueue(activity!!.applicationContext)

        //jobList = new ArrayList<>();
        progressBar1 = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        recyclerView = view.findViewById<View>(R.id.contacts_list) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.addItemDecoration(SimpleDividerItemDecoration(
                activity!!.applicationContext
        ))
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView!!.layoutManager = layoutManager
        jsonArray
        //---------------

      setHasOptionsMenu(true)
        return view
    }
    private val jsonArray: Unit
        private get() {
            progressBar1!!.visibility = View.VISIBLE
            userList = ArrayList()
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
                                    user.id = jsonObject.getInt("id_user")
                                    user.picture = jsonObject.getString("picture")
                                    user.type = jsonObject.getString("type")
                                    user.name = jsonObject.getString("name")
                                    user.last_name = jsonObject.getString("last_name")
                                    //-----------------------------------------
                                    userList!!.add(user)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            adapter = ContactsCompanyAdapter(activity!!, userList!!)
                            adapter!!.notifyDataSetChanged()
                            recyclerView!!.adapter = adapter
                            adapter!!.notifyDataSetChanged()
                            progressBar1!!.visibility = View.GONE
                            url = GlobalParams.url + "/getapprouvedjobappbycompany/"
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }

    companion object {
        const val NICKNAME = "usernickname"
    }
}