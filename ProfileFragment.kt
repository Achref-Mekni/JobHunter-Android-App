package com.esprit.jobhunter.MenuFragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.*
import com.esprit.jobhunter.MainActivity
import com.esprit.jobhunter.MyUtils.AndroidMultiPartEntity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.ProfileAdapters.*
import com.squareup.picasso.Picasso
import com.whiteelephant.monthpicker.MonthPickerDialog
import de.hdodenhof.circleimageview.CircleImageView
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var skills_tv: TextView? = null
    private var recyclerViewEduc: RecyclerView? = null
    private var recyclerViewExp: RecyclerView? = null
    private var recyclerViewCert: RecyclerView? = null
    private var recyclerViewLang: RecyclerView? = null
    private var recyclerViewVol: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var profileNestedScrollView: NestedScrollView? = null
    private var progressBarProfile: ProgressBar? = null
    private var educationList: ArrayList<Education>? = null
    private var experienceList: ArrayList<Experience>? = null
    private var certificationList: ArrayList<Certification>? = null
    private var volunteerList: ArrayList<Volunteer>? = null
    private var languageList: ArrayList<Language>? = null
    private var skillsList: ArrayList<Skills>? = null
    private var adapterEduc: RecyclerView.Adapter<*>? = null
    private var adapterExp: RecyclerView.Adapter<*>? = null
    private var adapterCert: RecyclerView.Adapter<*>? = null
    private var adapterLang: RecyclerView.Adapter<*>? = null
    private var adapterVol: RecyclerView.Adapter<*>? = null
    var queue: RequestQueue? = null
    var url: String? = GlobalParams.url + "/education/"
    var url2: String? = GlobalParams.url + "/experience/"
    var url3: String? = GlobalParams.url + "/certification/"
    var url4: String? = GlobalParams.url + "/language/"
    var url5: String? = GlobalParams.url + "/volunteer/"
    var url6 = GlobalParams.url + "/skills/"
    var connectedUser: User? = null
    var textName: TextView? = null
    var textDescription: TextView? = null
    var textAdress: TextView? = null
    var textEduc: TextView? = null
    var tvTitle: TextView? = null
    var tvEmail: TextView? = null
    var tvTel: TextView? = null
    var tvNat: TextView? = null
    var profilePic: CircleImageView? = null
    var addVolun: ImageView? = null
    var addEduc: ImageView? = null
    var addExp: ImageView? = null
    var addSkill: ImageView? = null
    var addCert: ImageView? = null
    var addLang: ImageView? = null
    var skills = ""
    var test1 = ""
    var test2 = ""

    //-----Uploadimage vars
    //Bitmap to get image from gallery
    private var bitmap: Bitmap? = null

    //Uri to store the image uri
    private var filePathUri: Uri? = null
    var UPLOAD_URL = GlobalParams.uploadUrl
    var totalSize: Long = 0
    private var fileUri: Uri? = null
    val CODE_GALLERY_REQUEST = 999
    var url7 = GlobalParams.url + "/addeduc"
    var url8 = GlobalParams.url + "/addexp"
    var url9 = GlobalParams.url + "/addskill"
    var url10 = GlobalParams.url + "/addcert"
    var url11 = GlobalParams.url + "/addlang"
    var url12 = GlobalParams.url + "/addevolun"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val photoHeader = view.findViewById<View>(R.id.photoHeader)
        photoHeader.translationZ = 6f
        photoHeader.invalidate()
        //----------------Getting connected user
        connectedUser = (activity as MainActivity?)!!.connectedUser
        //----------------
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        //-----------------------------
        profileNestedScrollView = view.findViewById<View>(R.id.nestedProfil) as NestedScrollView
        progressBarProfile = view.findViewById<View>(R.id.progressBarProfil) as ProgressBar
        textName = view.findViewById<View>(R.id.tvName) as TextView
        textDescription = view.findViewById<View>(R.id.tvSummary) as TextView
        textAdress = view.findViewById<View>(R.id.tvAddress) as TextView
        textEduc = view.findViewById<View>(R.id.tvEducation) as TextView
        profilePic = view.findViewById<View>(R.id.civProfilePic) as CircleImageView
        tvTitle = view.findViewById<View>(R.id.tvTitle) as TextView
        tvEmail = view.findViewById<View>(R.id.tvEmail) as TextView
        tvTel = view.findViewById<View>(R.id.tvTel) as TextView
        tvNat = view.findViewById<View>(R.id.tvNat) as TextView
        addEduc = view.findViewById<View>(R.id.addEduc) as ImageView
        addExp = view.findViewById<View>(R.id.addExp) as ImageView
        addSkill = view.findViewById<View>(R.id.addSkill) as ImageView
        addCert = view.findViewById<View>(R.id.addCert) as ImageView
        addLang = view.findViewById<View>(R.id.addLang) as ImageView
        addVolun = view.findViewById<View>(R.id.addVolun) as ImageView
        profilePic!!.translationZ = 20f
        profilePic!!.invalidate()
        if (connectedUser!!.name != null && connectedUser!!.name != "null" && connectedUser!!.last_name != null && connectedUser!!.last_name != "null") {
            textName!!.text = connectedUser!!.name + " " + connectedUser!!.last_name
        }
        if (connectedUser!!.adress != null && connectedUser!!.adress != "null") {
            textAdress!!.text = connectedUser!!.adress
        }
        if (connectedUser!!.description != null && connectedUser!!.description != "null") {
            textDescription!!.text = connectedUser!!.description
        }
        if (connectedUser!!.tel1 != null && connectedUser!!.tel1 != "null") {
            test1 = "Tel1: " + connectedUser!!.tel1
        }
        var cont = ""
        if (connectedUser!!.tel2 != null && connectedUser!!.tel2 != "null") {
            test2 = "Tel2: " + connectedUser!!.tel2
        }
        if (test1.isEmpty() && test2.isEmpty()) cont = cont + ""
        if (!test1.isEmpty() && !test2.isEmpty()) cont = "$cont$test1 - $test2"
        if (test1.isEmpty() && !test2.isEmpty()) cont = cont + test1
        if (!test1.isEmpty() && test2.isEmpty()) cont = cont + test2
        tvTel!!.text = cont
        if (connectedUser!!.nationality != null && connectedUser!!.nationality != "null") {
            tvNat!!.text = connectedUser!!.nationality
        }
        if (connectedUser!!.email != null && connectedUser!!.email != "null") {
            tvEmail!!.text = connectedUser!!.email
        }
        if (connectedUser!!.picture != null && connectedUser!!.picture != "null") {

            if (connectedUser!!.picture!!.length > 60) {
                Picasso.with(activity!!.applicationContext).load(connectedUser!!.picture).into(profilePic)
            } else {
                Picasso.with(activity!!.applicationContext).load(GlobalParams.ressourceUrl + "/" + connectedUser!!.picture).into(profilePic)
            }

        }
        //-----------------------------Education
        url += connectedUser!!.cv_id
        jsonArrayEducation
        recyclerViewEduc = view.findViewById<View>(R.id.recyclerEducation) as RecyclerView
        recyclerViewEduc!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerViewEduc!!.layoutManager = layoutManager


        //---------------------Experience
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        url2 += connectedUser!!.cv_id
        jsonArrayExperience
        recyclerViewExp = view.findViewById<View>(R.id.recyclerExperience) as RecyclerView
        recyclerViewExp!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerViewExp!!.layoutManager = layoutManager

        //---------------------Skills
        skills_tv = view.findViewById(R.id.skills_tv)
        jsonArraySkills

        //---------------------Certifications
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        url3 += connectedUser!!.cv_id
        jsonArrayCertification
        recyclerViewCert = view.findViewById<View>(R.id.recyclerCertification) as RecyclerView
        recyclerViewCert!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerViewCert!!.layoutManager = layoutManager

        //---------------------Languages
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        url4 += connectedUser!!.cv_id
        jsonArrayLanguage
        recyclerViewLang = view.findViewById<View>(R.id.recyclerLanguages) as RecyclerView
        recyclerViewLang!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerViewLang!!.layoutManager = layoutManager

        //---------------------Volunteer
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        url5 += connectedUser!!.cv_id
        jsonArrayVolunteer
        recyclerViewVol = view.findViewById<View>(R.id.recyclerVolunteer) as RecyclerView
        recyclerViewVol!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerViewVol!!.layoutManager = layoutManager


        //---------------------Image upload
        profilePic!!.setOnClickListener { requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CODE_GALLERY_REQUEST) }

        //---------------------
        addEduc!!.setOnClickListener(addEducOnClickListener)
        addExp!!.setOnClickListener(addExpOnClickListener)
        addSkill!!.setOnClickListener(addSkillOnClickListener)
        addCert!!.setOnClickListener(addCertOnClickListener)
        addLang!!.setOnClickListener(addLangOnClickListener)
        addVolun!!.setOnClickListener(addVolunOnClickListener)
        //---------------------
        return view
    }

    //==================================
    private val addEducOnClickListener = View.OnClickListener { addEducClicked() }

    private fun addEducClicked() {
        val dialogBuilder = AlertDialog.Builder(activity).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_educ_dialog, null)

        val button1 = dialogView.findViewById<View>(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById<View>(R.id.buttonCancel) as Button
        val inst_name = dialogView.findViewById<View>(R.id.edt_inst) as EditText
        val start_date = dialogView.findViewById<View>(R.id.edt_from) as EditText
        val end_date = dialogView.findViewById<View>(R.id.edt_to) as EditText
        val degree = dialogView.findViewById<View>(R.id.edt_degree) as EditText
        val domain = dialogView.findViewById<View>(R.id.edt_domain) as EditText
        val result = dialogView.findViewById<View>(R.id.edt_result) as EditText
        val description = dialogView.findViewById<View>(R.id.edt_desc) as EditText
        val today = Calendar.getInstance()
        start_date.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    start_date.setText("")
                    start_date.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        end_date.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    end_date.setText("")
                    end_date.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        button2.setOnClickListener { dialogBuilder.dismiss() }
        button1.setOnClickListener { // DO SOMETHINGS
            if (inst_name.text.toString() == "" || start_date.text.toString() == "" || end_date.text.toString() == "" || degree.text.toString() == "" || domain.text.toString() == "" || result.text.toString() == "" || description.text.toString() == "") {
                val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                alertDialog.setTitle("Alert!")
                alertDialog.setMessage("Please fill all the fields")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                val postRequest: StringRequest = object : StringRequest(Method.POST, url7,
                        Response.Listener { jsonArrayEducation },
                        Response.ErrorListener { error -> // error
                            error.printStackTrace()
                        }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["inst_name"] = inst_name.text.toString()
                        params["start_date"] = start_date.text.toString()
                        params["end_planned_date"] = end_date.text.toString()
                        params["degree"] = degree.text.toString()
                        params["domain"] = domain.text.toString()
                        params["result"] = result.text.toString()
                        params["description"] = description.text.toString()
                        params["cv_id"] = connectedUser!!.cv_id!!
                        return params
                    }
                }
                val queuex = Volley.newRequestQueue(activity!!.applicationContext)
                queuex.add(postRequest)
            }
            //------------------
            dialogBuilder.dismiss()
            jsonArrayEducation
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private val addExpOnClickListener = View.OnClickListener { addExpClicked() }

    private fun addExpClicked() {
        val today = Calendar.getInstance()
        val dialogBuilder = AlertDialog.Builder(activity).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_exp_dialog, null)

        val button1 = dialogView.findViewById<View>(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById<View>(R.id.buttonCancel) as Button
        val edt_label = dialogView.findViewById<View>(R.id.edt_label) as EditText
        val edt_establishment = dialogView.findViewById<View>(R.id.edt_establishment) as EditText
        val edt_type = dialogView.findViewById<View>(R.id.edt_type) as EditText
        val edt_place = dialogView.findViewById<View>(R.id.edt_place) as EditText
        val edt_desc = dialogView.findViewById<View>(R.id.edt_desc) as EditText
        val edt_to = dialogView.findViewById<View>(R.id.edt_to) as EditText
        val edt_from = dialogView.findViewById<View>(R.id.edt_from) as EditText
        val still_going = dialogView.findViewById<View>(R.id.still_going) as CheckBox


        edt_from.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    edt_from.setText("")
                    edt_from.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        edt_to.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    edt_to.setText("")
                    edt_to.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        button2.setOnClickListener { dialogBuilder.dismiss() }
        button1.setOnClickListener { // DO SOMETHINGS
            if (edt_label.text.toString() == "" || edt_establishment.text.toString() == "" || edt_type.text.toString() == "" || edt_place.text.toString() == "" || edt_desc.text.toString() == "" || edt_from.text.toString() == "") {
                val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                alertDialog.setTitle("Alert!")
                alertDialog.setMessage("Please fill all the fields")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                val postRequest: StringRequest = object : StringRequest(Method.POST, url8,
                        Response.Listener { jsonArrayExperience },
                        Response.ErrorListener { error -> // error
                            error.printStackTrace()
                        }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["label"] = edt_label.text.toString()
                        params["establishment_name"] = edt_establishment.text.toString()
                        params["type"] = edt_type.text.toString()
                        params["place"] = edt_place.text.toString()
                        params["description"] = edt_desc.text.toString()
                        params["start_date"] = edt_from.text.toString()
                        params["end_date"] = edt_to.text.toString()
                        if (still_going.isChecked) {
                            params["still_going"] = "1"
                        } else {
                            params["still_going"] = "0"
                        }
                        params["cv_id"] = connectedUser!!.cv_id!!
                        return params
                    }
                }
                val queuex = Volley.newRequestQueue(activity!!.applicationContext)
                queuex.add(postRequest)
            }
            //------------------
            dialogBuilder.dismiss()
            jsonArrayExperience
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private val addSkillOnClickListener = View.OnClickListener { addSkillClicked() }

    private fun addSkillClicked() {
        val dialogBuilder = AlertDialog.Builder(activity).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_skill_dialog, null)

        val button1 = dialogView.findViewById<View>(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById<View>(R.id.buttonCancel) as Button
        val edt_label = dialogView.findViewById<View>(R.id.edt_label) as EditText
        button2.setOnClickListener { dialogBuilder.dismiss() }
        button1.setOnClickListener { // DO SOMETHINGS
            if (edt_label.text.toString() == "") {
                val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                alertDialog.setTitle("Alert!")
                alertDialog.setMessage("Please fill all the fields")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                val postRequest: StringRequest = object : StringRequest(Method.POST, url9,
                        Response.Listener { jsonArraySkills },
                        Response.ErrorListener { error -> // error
                            error.printStackTrace()
                        }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["label"] = edt_label.text.toString()
                        params["cv_id"] = connectedUser!!.cv_id!!
                        return params
                    }
                }
                val queuex = Volley.newRequestQueue(activity!!.applicationContext)
                queuex.add(postRequest)
            }
            //------------------
            dialogBuilder.dismiss()
            jsonArraySkills
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private val addCertOnClickListener = View.OnClickListener { addCertClicked() }

    private fun addCertClicked() {
        val today = Calendar.getInstance()
        val dialogBuilder = AlertDialog.Builder(activity).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_certif_dialog, null)

        val button1 = dialogView.findViewById<View>(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById<View>(R.id.buttonCancel) as Button
        val edt_label = dialogView.findViewById<View>(R.id.edt_label) as EditText
        val edt_authority = dialogView.findViewById<View>(R.id.edt_authority) as EditText
        val edt_licence = dialogView.findViewById<View>(R.id.edt_licence) as EditText
        val edt_to = dialogView.findViewById<View>(R.id.edt_to) as EditText
        val edt_from = dialogView.findViewById<View>(R.id.edt_from) as EditText
        val still_going = dialogView.findViewById<View>(R.id.still_going) as CheckBox


        edt_from.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    edt_from.setText("")
                    edt_from.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        edt_to.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    edt_to.setText("")
                    edt_to.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        button2.setOnClickListener { dialogBuilder.dismiss() }
        button1.setOnClickListener { // DO SOMETHINGS
            if (edt_label.text.toString() == "" || edt_authority.text.toString() == "" || edt_licence.text.toString() == "" || edt_from.text.toString() == "") {
                val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                alertDialog.setTitle("Alert!")
                alertDialog.setMessage("Please fill all the fields")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                val postRequest: StringRequest = object : StringRequest(Method.POST, url10,
                        Response.Listener { jsonArrayCertification },
                        Response.ErrorListener { error -> // error
                            error.printStackTrace()
                        }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["label"] = edt_label.text.toString()
                        params["cert_authority"] = edt_authority.text.toString()
                        params["licence_num"] = edt_licence.text.toString()
                        params["cert_date"] = edt_from.text.toString()
                        params["expire_date"] = edt_to.text.toString()
                        if (still_going.isChecked) {
                            params["if_expire"] = "1"
                        } else {
                            params["if_expire"] = "0"
                        }
                        params["cv_id"] = connectedUser!!.cv_id!!
                        return params
                    }
                }
                val queuex = Volley.newRequestQueue(activity!!.applicationContext)
                queuex.add(postRequest)
            }
            //------------------
            dialogBuilder.dismiss()
            jsonArrayCertification
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private val addLangOnClickListener = View.OnClickListener { addLangClicked() }

    private fun addLangClicked() {
        val dialogBuilder = AlertDialog.Builder(activity).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_lang_dialog, null)

        val button1 = dialogView.findViewById<View>(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById<View>(R.id.buttonCancel) as Button
        val edt_label = dialogView.findViewById<View>(R.id.edt_label) as EditText
        val edt_level = dialogView.findViewById<View>(R.id.edt_level) as Spinner
        button2.setOnClickListener { dialogBuilder.dismiss() }
        button1.setOnClickListener { // DO SOMETHINGS
            if (edt_label.text.toString() == "" || edt_level.selectedItem.toString() == "") {
                val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                alertDialog.setTitle("Alert!")
                alertDialog.setMessage("Please fill all the fields")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                val postRequest: StringRequest = object : StringRequest(Method.POST, url11,
                        Response.Listener { jsonArrayLanguage },
                        Response.ErrorListener { error -> // error
                            error.printStackTrace()
                        }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["label"] = edt_label.text.toString()
                        params["level"] = edt_level.selectedItem.toString()
                        params["cv_id"] = connectedUser!!.cv_id!!
                        return params
                    }
                }
                val queuex = Volley.newRequestQueue(activity!!.applicationContext)
                queuex.add(postRequest)
            }
            //------------------
            dialogBuilder.dismiss()
            jsonArrayLanguage
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private val addVolunOnClickListener = View.OnClickListener { addVolunClicked() }

    private fun addVolunClicked() {
        val today = Calendar.getInstance()
        val dialogBuilder = AlertDialog.Builder(activity).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_volunteer_dialog, null)

        val button1 = dialogView.findViewById<View>(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById<View>(R.id.buttonCancel) as Button
        val edt_organisation = dialogView.findViewById<View>(R.id.edt_organisation) as EditText
        val edt_role = dialogView.findViewById<View>(R.id.edt_role) as EditText
        val edt_domain = dialogView.findViewById<View>(R.id.edt_domain) as EditText
        val edt_desc = dialogView.findViewById<View>(R.id.edt_desc) as EditText
        val edt_to = dialogView.findViewById<View>(R.id.edt_to) as EditText
        val edt_from = dialogView.findViewById<View>(R.id.edt_from) as EditText
        val still_going = dialogView.findViewById<View>(R.id.still_going) as CheckBox


        edt_from.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    edt_from.setText("")
                    edt_from.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        edt_to.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //------------------
                val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear -> //Toast.makeText(getActivity(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                    edt_to.setText("")
                    edt_to.setText((selectedMonth + 1).toString() + "/" + selectedYear)
                }, today[Calendar.YEAR], today[Calendar.MONTH])
                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(1900)
                        .setActivatedYear(2019)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        .setOnMonthChangedListener { }
                        .setOnYearChangedListener { }
                        .build()
                        .show()
                //-------------------
            }
            true
        }
        button2.setOnClickListener { dialogBuilder.dismiss() }
        button1.setOnClickListener { // DO SOMETHINGS
            if (edt_organisation.text.toString() == "" || edt_role.text.toString() == "" || edt_domain.text.toString() == "" || edt_desc.text.toString() == "" || edt_from.text.toString() == "") {
                val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
                alertDialog.setTitle("Alert!")
                alertDialog.setMessage("Please fill all the fields")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                val postRequest: StringRequest = object : StringRequest(Method.POST, url12,
                        Response.Listener { jsonArrayVolunteer },
                        Response.ErrorListener { error -> // error
                            error.printStackTrace()
                        }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["organisation"] = edt_organisation.text.toString()
                        params["role"] = edt_role.text.toString()
                        params["domain"] = edt_domain.text.toString()
                        params["start_date"] = edt_from.text.toString()
                        params["end_date"] = edt_to.text.toString()
                        params["description"] = edt_desc.text.toString()
                        if (still_going.isChecked) {
                            params["still_going"] = "1"
                        } else {
                            params["still_going"] = "0"
                        }
                        params["cv_id"] = connectedUser!!.cv_id!!
                        return params
                    }
                }
                val queuex = Volley.newRequestQueue(activity!!.applicationContext)
                queuex.add(postRequest)
            }
            //------------------
            dialogBuilder.dismiss()
            jsonArrayVolunteer
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    //==================================
    private val jsonArraySkills: Unit
        private get() {
            skillsList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url6 + connectedUser!!.cv_id,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            if (response.getInt("success") == 1) {
                                jsonArray = response.getJSONArray("result")

                                for (i in 0 until jsonArray.length()) {
                                    try {
                                        val jsonObject = jsonArray.getJSONObject(i)
                                        val skill = Skills()
                                        skill.id = jsonObject.getInt("id")
                                        skill.label = jsonObject.getString("label")
                                        skill.cv_id = jsonObject.getInt("cv_id")
                                        skillsList!!.add(skill)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                                skills = ""
                                if (skillsList!!.size == 0) {
                                    skills_tv!!.text = "None"
                                } else {
                                    for (i in skillsList!!.indices) {
                                        skills = if (i != skillsList!!.size - 1) {
                                            skills + skillsList!![i].label + ", "
                                        } else {
                                            skills + skillsList!![i].label
                                        }
                                    }
                                }
                                skills_tv!!.text = skills
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error -> error.printStackTrace() }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }

    //------------------------------------------------------
    private val jsonArrayEducation: Unit
        private get() {
            educationList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            if (response.getInt("success") == 1) {
                                jsonArray = response.getJSONArray("result")

                                for (i in 0 until jsonArray.length()) {
                                    try {
                                        val jsonObject = jsonArray.getJSONObject(i)
                                        val education = Education()
                                        education.id = jsonObject.getInt("id")
                                        education.inst_name = jsonObject.getString("inst_name")
                                        education.start_date = jsonObject.getString("start_date")
                                        education.end_date = jsonObject.getString("end_planned_date")
                                        education.degree = jsonObject.getString("degree")
                                        education.domain = jsonObject.getString("domain")
                                        education.result = jsonObject.getString("result")
                                        educationList!!.add(education)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                                if (educationList!![0].inst_name!!.length > 33) {
                                    textEduc!!.setLines(2)
                                } else if (educationList!![0].inst_name!!.length > 66) {
                                    textEduc!!.setLines(3)
                                } else if (educationList!![0].inst_name!!.length > 132) {
                                    textEduc!!.setLines(4)
                                }
                                textEduc!!.text = educationList!![0].inst_name
                            } else if (response.getInt("success") == 0) {
                                recyclerViewEduc!!.visibility = View.GONE
                            }
                            if (educationList!!.size != 0) textEduc!!.text = educationList!![0].inst_name else textEduc!!.text = "-"
                            adapterEduc = EducationAdapter(activity!!.applicationContext, educationList!!)
                            recyclerViewEduc!!.adapter = adapterEduc
                            adapterEduc!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        println("MYYYLIST$educationList")
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }

    //------------------------------------------------------
    private val jsonArrayExperience: Unit
        private get() {
            experienceList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url2,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            if (response.getInt("success") == 1) {
                                jsonArray = response.getJSONArray("result")

                                for (i in 0 until jsonArray.length()) {
                                    try {
                                        val jsonObject = jsonArray.getJSONObject(i)
                                        val experience = Experience()
                                        experience.id = jsonObject.getInt("id")
                                        experience.label = jsonObject.getString("label")
                                        experience.start_date = jsonObject.getString("start_date")
                                        experience.end_date = jsonObject.getString("end_date")
                                        experience.description = jsonObject.getString("description")
                                        experience.still_going = jsonObject.getInt("still_going")
                                        experience.establishmentName = jsonObject.getString("establishment_name")
                                        experienceList!!.add(experience)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                                if (experienceList!![0].establishmentName!!.length > 33) {
                                    tvTitle!!.setLines(2)
                                } else if (experienceList!![0].establishmentName!!.length > 66) {
                                    tvTitle!!.setLines(3)
                                } else if (experienceList!![0].establishmentName!!.length > 132) {
                                    tvTitle!!.setLines(4)
                                }
                                tvTitle!!.text = experienceList!![0].label + " at " + experienceList!![0].establishmentName
                            } else if (response.getInt("success") == 0) {
                                recyclerViewExp!!.visibility = View.GONE
                            }
                            adapterExp = ExperienceAdapter(activity!!.applicationContext, experienceList!!)
                            recyclerViewExp!!.adapter = adapterExp
                            adapterExp!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        println("MYYYLIST$experienceList")
                    },
                    Response.ErrorListener { error -> error.printStackTrace() }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }

    //------------------------------------------------------
    private val jsonArrayCertification: Unit
        private get() {
            certificationList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url3,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            if (response.getInt("success") == 1) {
                                jsonArray = response.getJSONArray("result")

                                for (i in 0 until jsonArray.length()) {
                                    try {
                                        val jsonObject = jsonArray.getJSONObject(i)
                                        val certif = Certification()
                                        certif.id = jsonObject.getInt("id")
                                        certif.label = jsonObject.getString("label")
                                        certif.cert_authority = jsonObject.getString("cert_authority")
                                        certif.licence_num = jsonObject.getString("licence_num")
                                        certif.if_expire = jsonObject.getInt("if_expire")
                                        certif.cert_date = jsonObject.getString("cert_date")
                                        if (jsonObject.getInt("if_expire") == 1) {
                                            certif.expire_date = jsonObject.getString("expire_date")
                                        }
                                        certificationList!!.add(certif)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            } else if (response.getInt("success") == 0) {
                                recyclerViewCert!!.visibility = View.GONE
                            }
                            adapterCert = CertificationsAdapter(activity!!.applicationContext, certificationList!!)
                            recyclerViewCert!!.adapter = adapterCert
                            adapterCert!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        println("MYYYLIST$certificationList")
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }

    //------------------------------------------------------
    private val jsonArrayLanguage: Unit
        private get() {
            languageList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url4,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            if (response.getInt("success") == 1) {
                                jsonArray = response.getJSONArray("result")

                                for (i in 0 until jsonArray.length()) {
                                    try {
                                        val jsonObject = jsonArray.getJSONObject(i)
                                        val language = Language()
                                        language.id = jsonObject.getInt("id")
                                        language.label = jsonObject.getString("label")
                                        language.level = jsonObject.getString("level")
                                        languageList!!.add(language)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            } else if (response.getInt("success") == 0) {
                                recyclerViewLang!!.visibility = View.GONE
                            }
                            adapterLang = LanguagesAdapter(activity!!.applicationContext, languageList!!)
                            recyclerViewLang!!.adapter = adapterLang
                            adapterLang!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        println("MYYYLIST$languageList")
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }

    //------------------------------------------------------
    private val jsonArrayVolunteer: Unit
        private get() {
            progressBarProfile!!.visibility = View.VISIBLE
            volunteerList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url5,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            if (response.getInt("success") == 1) {
                                jsonArray = response.getJSONArray("result")

                                for (i in 0 until jsonArray.length()) {
                                    try {
                                        val jsonObject = jsonArray.getJSONObject(i)
                                        val vol = Volunteer()
                                        vol.id = jsonObject.getInt("id")
                                        vol.organisation = jsonObject.getString("organisation")
                                        vol.role = jsonObject.getString("role")
                                        vol.start_date = jsonObject.getString("start_date")
                                        vol.still_going = jsonObject.getInt("still_going")
                                        vol.end_date = "Today"
                                        if (jsonObject.getInt("still_going") == 0) {
                                            vol.end_date = jsonObject.getString("end_date")
                                        }
                                        volunteerList!!.add(vol)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            } else if (response.getInt("success") == 0) {
                                recyclerViewVol!!.visibility = View.GONE
                            }
                            adapterVol = VolunteerAdapter(activity!!.applicationContext, volunteerList!!)
                            recyclerViewVol!!.adapter = adapterVol
                            adapterVol!!.notifyDataSetChanged()
                            progressBarProfile!!.visibility = View.GONE
                            profileNestedScrollView!!.alpha = 1f
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        println("MYYYLIST$volunteerList")
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }

    //------------------------------------------------------
    //PERSSION TO ACCESS GALERY
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST)
            } else {
                Toast.makeText(activity!!.applicationContext, "You don't have permission to access gallery", Toast.LENGTH_LONG).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getPath(uri: Uri?): String {
        var cursor = activity!!.contentResolver.query(uri, null, null, null, null)
        cursor.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = activity!!.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null)
        cursor.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }

    // PUT IMAGE ON IMAGE VIEW AFTER CHOOSING
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            filePathUri = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, filePathUri)
                profilePic!!.setImageBitmap(bitmap)
                UploadFileToServer().execute()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //CONVERT IMAGE TO BASE64 STRING
    private fun imageToString(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    //======================================================

    private inner class UploadFileToServer : AsyncTask<Void?, Int?, String?>() {
        override fun onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute()
        }

        protected override fun onProgressUpdate(vararg values: Int?) {
            // Making progress bar visible
            progressBarProfile!!.bringToFront()
            progressBarProfile!!.invalidate()
            progressBarProfile!!.visibility = View.VISIBLE
        }

        protected override fun doInBackground(vararg p0: Void?): String? {
            return uploadFile()
        }

        private fun uploadFile(): String? {
            var responseString: String? = null
            val httpclient: HttpClient = DefaultHttpClient()
            val httppost = HttpPost(UPLOAD_URL)
            val client: HttpClient = DefaultHttpClient()
            try {
                val entity = AndroidMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, object : AndroidMultiPartEntity.ProgressListener {
                    override fun transferred(num: Long) {
                        publishProgress((num / totalSize.toFloat() * 100).toInt())
                    }
                })
                val sourceFile = File(getPath(filePathUri))
                // Adding file data to http body
                entity.addPart("file", FileBody(sourceFile))
                // Adding user_id
                entity.addPart("user_id", StringBody(java.lang.String.valueOf(connectedUser!!.id), Charset.forName("UTF-8")))
                totalSize = entity.contentLength
                httppost.entity = entity

                // Making server call
                val response = httpclient.execute(httppost)
                val r_entity = response.entity
                val statusCode = response.statusLine.statusCode
                responseString = if (statusCode == 200) {
                    // Server response
                    EntityUtils.toString(r_entity)
                } else {
                    "Error occurred! Http Status Code: $statusCode"
                }
            } catch (e: ClientProtocolException) {
                responseString = e.toString()
            } catch (e: IOException) {
                responseString = e.toString()
            }
            return responseString
        }

        override fun onPostExecute(result: String?) {
            // showing the server response in an alert dialog
            progressBarProfile!!.visibility = View.GONE
            showAlert(result)
            super.onPostExecute(result)
        }
    }

    /**
     * Method to show alert dialog
     */
    private fun showAlert(message: String?) {
        if (message!!.contains("jpg") || message.contains("jpeg") || message.contains("png")) {
            val alertDialog = AlertDialog.Builder(activity, R.style.MyDialogTheme).create()
            alertDialog.setTitle("Image upload")
            alertDialog.setMessage("Your profile image has been Changed successfully !")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
            ) { dialog, which -> dialog.dismiss() }
            alertDialog.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            fileUri = savedInstanceState.getParcelable("file_uri")
        }
    }

    fun refreshFragment() {
        val ft = fragmentManager!!.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
    }
}