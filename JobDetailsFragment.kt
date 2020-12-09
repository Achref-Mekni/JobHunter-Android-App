package com.esprit.jobhunter.OffersFragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import co.lujun.androidtagview.TagContainerLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.Job
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.esprit.jobhunter.SQLite.DatabaseHelper
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class JobDetailsFragment : Fragment() {
    var job = Job()
    var x: Job? = null
    var text = ""
    var job_label: TextView? = null
    var start_date: TextView? = null
    var contract_type: TextView? = null
    var i = 0
    var mTagContainerLayout: TagContainerLayout? = null
    var career_req: TextView? = null
    var description: TextView? = null
    var list: MutableList<String> = ArrayList()
    var cmp_pic: ImageView? = null
    var apply: Button? = null
    var show_profile: Button? = null
    var bookmark: CheckBox? = null
    private var offerJobDetailNestedScrollView: NestedScrollView? = null
    private var progressBarOfferJobDetail: ProgressBar? = null
    var user: User? = null
    var url: String? = null
    var queue: RequestQueue? = null
    var applyVerify = 0
    var if_bookmarked: Boolean? = null
    private val storageJobList: MutableList<Job> = ArrayList()
    private var db: DatabaseHelper? = null
    fun setData(job: Job) {
        this.job = job
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_job_details, container, false)
        i = 1
        offerJobDetailNestedScrollView = view.findViewById(R.id.nestedJobOfferDetails)
        progressBarOfferJobDetail = view.findViewById(R.id.progressBarJobOfferDetails)
        bookmark = view.findViewById<View>(R.id.bookmark) as CheckBox
        queue = Volley.newRequestQueue(activity)
        val fromLogin = activity!!.intent
        user = fromLogin.getSerializableExtra("connectedUser") as User
        job_label = view.findViewById(R.id.job_label)
        start_date = view.findViewById(R.id.start_date)
        contract_type = view.findViewById(R.id.contract_type)
        career_req = view.findViewById(R.id.career_req)
        description = view.findViewById(R.id.description)
        cmp_pic = view.findViewById(R.id.cmp_pic)
        apply = view.findViewById(R.id.apply)
        show_profile = view.findViewById(R.id.show_profile)
        job_label!!.setText(job.label)
        start_date!!.setText(job.start_date)
        contract_type!!.setText(job.contract_type)
        career_req!!.setText(job.career_req)
        description!!.setText(job.description)

        if (job.company_pic!!.length > 60) {
            Picasso.with(activity!!.applicationContext).load(job.company_pic).into(cmp_pic)
        } else {
            Picasso.with(activity!!.applicationContext).load(GlobalParams.ressourceUrl + "/" + job.company_pic).into(cmp_pic)
        }
        println("HHHHHHHHHHHHHHHHHHH+++: $job")
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
        println("NAAAAAAAAAAAAAAAAAAAME: " + job.company_name)
        println("NAAAAAAAAAAAAAAAAAAAME: " + job.company_pic)
        verifyApplications()
        apply!!.setOnClickListener(View.OnClickListener {
            if (applyVerify == 0) {
                progressBarOfferJobDetail!!.setVisibility(View.VISIBLE)
                url = GlobalParams.url + "/addappjob/" + user!!.id.toString() + "/" + job.id
                val getData = StringRequest(Request.Method.GET, url, Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        println(obj)
                        if (obj.getInt("success") == 1) {
                            apply!!.setEnabled(false)
                        }
                        progressBarOfferJobDetail!!.setVisibility(View.GONE)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    error.printStackTrace()
                })
                queue!!.add(getData)
                url = ""
                apply!!.setEnabled(false)
            }
        })

        //------Bookmark checked
        db = DatabaseHelper(activity)
        if_bookmarked = false
        bookmark!!.isChecked = false
        storageJobList.addAll(db!!.allJobs)
        for (jobItem in storageJobList) {
            if (jobItem.id == job.id) {
                if_bookmarked = true
                bookmark!!.isChecked = true
            }
        }
        bookmark!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == true) {
                db!!.insertJob(job)
                bookmark!!.isChecked = true
            } else if (isChecked == false) {
                db!!.deleteJob(job)
                bookmark!!.isChecked = false
            }
        }
        return view
    }

    fun verifyApplications() {
        progressBarOfferJobDetail!!.visibility = View.VISIBLE
        url = GlobalParams.url + "/getjobappbyuser/" + user!!.id.toString() + "/" + job.id
        val getData = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                if (obj.getInt("success") == 1) {
                    apply!!.isEnabled = false
                    applyVerify = 1
                    val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                    alertDialog.setTitle("Information")
                    alertDialog.setMessage("You have already applied to this job offer")
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()
                }
                if (obj.getInt("success") == 0) {
                    applyVerify = 0
                }
                progressBarOfferJobDetail!!.visibility = View.GONE
                offerJobDetailNestedScrollView!!.alpha = 1f
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()
        })
        queue!!.add(getData)
        url = ""
    }
}