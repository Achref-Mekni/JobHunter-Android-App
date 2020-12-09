package com.esprit.jobhunter.CompanyFragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.Job
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
class UpdateactionFragment : Fragment() {
    var queue: RequestQueue? = null
    var submit: Button? = null
    var delete: Button? = null
    var add: Button? = null
    var url1: String? = null
    var url2: String? = null
    var i = 1
    var user: User? = null
    var text = ""
    var type: String? = null
    var mTagContainerLayout: TagContainerLayout? = null
    var Title: EditText? = null
    var Description: EditText? = null
    var date_begin: EditText? = null
    var experience: EditText? = null
    var skills: EditText? = null
    var content_type: Spinner? = null
    var fragment: Fragment? = null
    var list: MutableList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val myActivity = activity as Main2Activity?
        val job = arguments!!["job"] as Job
        println("job$job")
        val fromLogin = activity!!.intent
        user = fromLogin.getSerializableExtra("connectedUser") as User
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_updateaction, container, false)
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        url1 = GlobalParams.url + "/updatejob/"
        url2 = GlobalParams.url + "/deletejob/"
        Title = view.findViewById<View>(R.id.Title) as EditText
        Description = view.findViewById<View>(R.id.Description) as EditText
        date_begin = view.findViewById<View>(R.id.date_begin) as EditText
        experience = view.findViewById<View>(R.id.experience) as EditText
        skills = view.findViewById<View>(R.id.skills) as EditText
        submit = view.findViewById<View>(R.id.submit) as Button
        content_type = view.findViewById<View>(R.id.content_type) as Spinner
        Title!!.setText(job.label)
        experience!!.setText(job.career_req)
        Description!!.setText(job.description)
        val tmp_str = job.start_date!!.replace("%", " ")
        date_begin!!.setText(tmp_str)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(this.context, R.array.options_arrays, R.layout.spinner_item)
        content_type!!.adapter = adapter
        Title!!.hint = "Title of offre"
        Title!!.setOnTouchListener { v, event ->
            Title!!.hint = ""
            false
        }
        Title!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                Title!!.hint = "Title of offre"
            }
        }
        Description!!.hint = "Description of offre"
        Description!!.setOnTouchListener { v, event ->
            Description!!.hint = ""
            false
        }
        Description!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                Description!!.hint = "Description of offre"
            }
        }
        date_begin!!.hint = "Date begin of the offer"
        date_begin!!.setOnTouchListener { v, event ->
            date_begin!!.hint = ""
            false
        }
        date_begin!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                date_begin!!.hint = "Date begin of the offer"
            }
        }
        experience!!.hint = "Experience for the offre"
        experience!!.setOnTouchListener { v, event ->
            experience!!.hint = ""
            false
        }
        experience!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                experience!!.hint = "Experience for the offre"
            }
        }
        skills!!.hint = "Skills required"
        skills!!.setOnTouchListener { v, event ->
            skills!!.hint = ""
            false
        }
        skills!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                skills!!.hint = "Skills required"
            }
        }
        add = view.findViewById<View>(R.id.add) as Button
        while (i < job.skills!!.length) {
            if (job.skills!![i].toString() != "," && job.skills!![i].toString() != "]") {
                text = text + job.skills!![i]
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
            url2 += job.id
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
            type = if (content_type!!.selectedItem.toString() == "Permenant") {
                job.contract_type
            } else {
                content_type!!.selectedItem.toString()
            }
            val tmp_str_2 = date_begin!!.text.toString().replace(" ", "%20")
            val dd = mTagContainerLayout!!.tags.toString()
            url1 += job.id.toString() + "/" + Title!!.text + "/" + Description!!.text + "/" + tmp_str_2.replace("/", "%2F") + "/" + type + "/" + experience!!.text + "/" + dd
            val getData = StringRequest(Request.Method.GET, url1, Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("success") == 1) {
                        Toast.makeText(activity!!.applicationContext, "Ligne inserré !", Toast.LENGTH_LONG).show()
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