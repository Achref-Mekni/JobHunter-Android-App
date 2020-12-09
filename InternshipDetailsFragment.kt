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
import com.esprit.jobhunter.Entity.Internship
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.esprit.jobhunter.SQLite.Database1Helper
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class InternshipDetailsFragment : Fragment() {
    var internship = Internship()
    var x: Internship? = null
    var text = ""
    var job_label: TextView? = null
    var start_date: TextView? = null
    var educ_req: TextView? = null
    var i = 1
    var mTagContainerLayout: TagContainerLayout? = null
    var duration: TextView? = null
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
    private val storageJobList: MutableList<Internship> = ArrayList()
    private var db: Database1Helper? = null
    fun setData(internship: Internship) {
        this.internship = internship
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_internship_details, container, false)
        offerJobDetailNestedScrollView = view.findViewById(R.id.nestedJobOfferDetails)
        progressBarOfferJobDetail = view.findViewById(R.id.progressBarJobOfferDetails)
        bookmark = view.findViewById<View>(R.id.bookmark) as CheckBox
        queue = Volley.newRequestQueue(activity)
        val fromLogin = activity!!.intent
        user = fromLogin.getSerializableExtra("connectedUser") as User
        job_label = view.findViewById(R.id.job_label)
        start_date = view.findViewById(R.id.start_date)
        duration = view.findViewById(R.id.duration)
        educ_req = view.findViewById(R.id.educ_req)
        description = view.findViewById(R.id.description)
        cmp_pic = view.findViewById(R.id.cmp_pic)
        apply = view.findViewById(R.id.apply)
        show_profile = view.findViewById(R.id.show_profile)
        job_label!!.setText(internship.label)
        start_date!!.setText(internship.start_date)
        duration!!.setText(internship.duration)
        educ_req!!.setText(internship.educ_req)
        description!!.setText(internship.description)

        if (internship.company_pic!!.length > 60) {
            Picasso.with(activity!!.applicationContext).load(internship.company_pic).into(cmp_pic)
        } else {
            Picasso.with(activity!!.applicationContext).load(GlobalParams.ressourceUrl + "/" + internship.company_pic).into(cmp_pic)
        }
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
        println("NAAAAAAAAAAAAAAAAAAAME: " + internship.company_name)
        println("NAAAAAAAAAAAAAAAAAAAME: " + internship.company_pic)
        verifyApplications()


        //------Bookmark checked
        db = Database1Helper(activity)
        if_bookmarked = false
        bookmark!!.isChecked = false
        storageJobList.addAll(db!!.allInternships)
        for (jobItem in storageJobList) {
            if (jobItem.id == internship.id) {
                if_bookmarked = true
                bookmark!!.isChecked = true
            }
        }
        bookmark!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == true) {
                db!!.insertInternship(internship)
                bookmark!!.isChecked = true
            } else if (isChecked == false) {
                db!!.deleteInternship(internship)
                bookmark!!.isChecked = false
            }
        }
        return view
    }

    fun verifyApplications() {
        progressBarOfferJobDetail!!.visibility = View.VISIBLE
        url = GlobalParams.url + "/getintappbyuser/" + user!!.id.toString() + "/" + internship.id
        val getData = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                if (obj.getInt("success") == 1) {
                    apply!!.isEnabled = false
                    applyVerify = 1
                    val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                    alertDialog.setTitle("Information")
                    alertDialog.setMessage("You have already applied to this internship offer")
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