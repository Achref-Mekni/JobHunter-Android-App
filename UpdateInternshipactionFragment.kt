package com.esprit.jobhunter.CompanyFragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.Internship
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.Main2Activity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class UpdateInternshipactionFragment : Fragment() {
    var queue: RequestQueue? = null
    var submit: Button? = null
    var delete: Button? = null
    var add: Button? = null
    var url1: String? = null
    var url2: String? = null
    var mTagContainerLayout: TagContainerLayout? = null
    var text = ""
    var i = 1
    var list: MutableList<String> = ArrayList()
    var user: User? = null
    var Title: EditText? = null
    var Description: EditText? = null
    var date_begin: EditText? = null
    var educ_req: EditText? = null
    var duration: EditText? = null
    var skills: EditText? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val myActivity = activity as Main2Activity?
        val internship = arguments!!["internship"] as Internship
        println("internship$internship")
        val fromLogin = activity!!.intent
        user = fromLogin.getSerializableExtra("connectedUser") as User
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_updateinternshipaction, container, false)
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        url1 = GlobalParams.url + "/updateinternship/"
        url2 = GlobalParams.url + "/deleteinternship/"
        Title = view.findViewById<View>(R.id.Title) as EditText
        Description = view.findViewById<View>(R.id.Description) as EditText
        date_begin = view.findViewById<View>(R.id.date_begin) as EditText
        educ_req = view.findViewById<View>(R.id.educ_req) as EditText
        submit = view.findViewById<View>(R.id.submit) as Button
        mTagContainerLayout = view.findViewById<View>(R.id.tagcontainerLayout) as TagContainerLayout
        skills = view.findViewById<View>(R.id.skills) as EditText
        add = view.findViewById<View>(R.id.add) as Button
        duration = view.findViewById<View>(R.id.duration) as EditText
        Title!!.setText(internship.label)
        Description!!.setText(internship.description)
        val tmp_str = internship.start_date!!.replace("%", " ")
        date_begin!!.setText(tmp_str)
        educ_req!!.setText(internship.educ_req)
        duration!!.setText(internship.duration)
        while (i < internship.skills!!.length) {
            if (internship.skills!![i].toString() != "," && internship.skills!![i].toString() != "]") {
                text = text + internship.skills!![i]
                println("textz  :$text")
            } else {
                list.add(text)
                println("list1$list")
                text = ""
            }
            i++
        }
        println("list2$list")
        mTagContainerLayout = view.findViewById<View>(R.id.tagcontainerLayout) as TagContainerLayout
        mTagContainerLayout!!.tags = list
        add!!.setOnClickListener { v: View? ->
            println(skills!!.text)
            list.add(skills!!.text.toString())
            mTagContainerLayout!!.tags = list
            skills!!.setText(null)
        }
        mTagContainerLayout!!.setOnTagClickListener(object : OnTagClickListener {
            override fun onTagClick(position: Int, text: String) {
                val a_builder = AlertDialog.Builder(activity, R.style.MyDialogTheme)
                a_builder.setMessage("Are you want delete this?")
                a_builder.setCancelable(false)
                a_builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                    mTagContainerLayout!!.removeTag(position)
                    list.removeAt(position)
                    dialog.cancel()
                }
                a_builder.setNegativeButton("cancel") { dialog: DialogInterface, which: Int -> dialog.cancel() }
                val alert = a_builder.create()
                alert.setTitle("Alert!")
                alert.show()
                println("zzz")
            }

            override fun onTagLongClick(position: Int, text: String) {
                // ...
            }

            fun onSelectedTagDrag(position: Int, text: String?) {
                // ...
            }

            override fun onTagCrossClick(position: Int) {
                // ...
            }
        })
        delete = view.findViewById<View>(R.id.delete) as Button
        delete!!.setOnClickListener {
            url2 += internship.id
            val getData = StringRequest(Request.Method.GET, url2, Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("success") == 1) {
                        Toast.makeText(activity!!.applicationContext, "update succeeded !", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    Log.e("JSONExeption", e.message)
                }
            }, Response.ErrorListener { error -> Log.e("ErrorResponse", error.message) })
            queue!!.add(getData)
            val bundle = Bundle()
            bundle.putSerializable("user", user)
            val myFragment: Fragment = MyOffersFragment()
            myFragment.arguments = bundle
            myActivity!!.supportFragmentManager.beginTransaction().replace(R.id.flContent, myFragment).addToBackStack(null).commit()
        }
        submit!!.setOnClickListener {
            val tmp_str_2 = date_begin!!.text.toString().replace(" ", "%20")
            url1 += internship.id.toString() + "/" + Title!!.text + "/" + Description!!.text + "/" + tmp_str_2.replace("/", "%2F") + "/" + educ_req!!.text + "/" + mTagContainerLayout!!.tags + "/" + duration!!.text

            val getData = StringRequest(Request.Method.GET, url1, Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("success") == 1) {
                        Toast.makeText(activity!!.applicationContext, "update succeeded !", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    Log.e("JSONExeption", e.message)
                }
            }, Response.ErrorListener { error -> println(error.message) })
            queue!!.add(getData)
            val bundle = Bundle()
            bundle.putSerializable("user", user)
            val myFragment: Fragment = MyOffersFragment()
            myFragment.arguments = bundle
            myActivity!!.supportFragmentManager.beginTransaction().replace(R.id.flContent, myFragment).addToBackStack(null).commit()
        }
        return view
    }
}