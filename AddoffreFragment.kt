package com.esprit.jobhunter.CompanyFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
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
class AddoffreFragment : DialogFragment() {
    var viewmore2: Button? = null
    var viewmore: Button? = null
    var queue: RequestQueue? = null
    var submit: Button? = null
    var add: Button? = null
    var url: String? = null
    var ur: String? = null
    private var calendar: Calendar? = null
    private val datePicker: DatePicker? = null
    private var year = 0
    private var month = 0
    private var day = 0
    var list: MutableList<String> = ArrayList()
    var Title: EditText? = null
    var Description: EditText? = null
    var date_begin: EditText? = null
    var experience: EditText? = null
    var skills: EditText? = null
    var content_type: Spinner? = null
    var button1: Button? = null
    var sp: TextView? = null
    var mTagContainerLayout: TagContainerLayout? = null
    var user: User? = null

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myActivity = activity as Main2Activity?
        val view = inflater.inflate(R.layout.fragment_addoffre, container, false)
        val fromLogin = activity!!.intent
        user = fromLogin.getSerializableExtra("connectedUser") as User
        println("user" + user!!.id)
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        url = GlobalParams.url + "/addjob/"
        Title = view.findViewById<View>(R.id.Title) as EditText
        Description = view.findViewById<View>(R.id.Description) as EditText
        date_begin = view.findViewById<View>(R.id.date_begin) as EditText
        add = view.findViewById<View>(R.id.add) as Button
        mTagContainerLayout = view.findViewById<View>(R.id.tagcontainerLayout) as TagContainerLayout
        experience = view.findViewById<View>(R.id.experience) as EditText
        skills = view.findViewById<View>(R.id.skills) as EditText
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
        /** */
        calendar = Calendar.getInstance()
        year = calendar!!.get(Calendar.YEAR)
        month = calendar!!.get(Calendar.MONTH)
        day = calendar!!.get(Calendar.DAY_OF_MONTH)
        showDate(year, month + 2, day)
        /** */
        submit = view.findViewById<View>(R.id.submit) as Button
        content_type = view.findViewById<View>(R.id.content_type) as Spinner
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(this.context, R.array.options_arrays, R.layout.spinner_item)
        content_type!!.adapter = adapter
        submit!!.setOnClickListener {
            ur = date_begin!!.text.toString()
            ur = ur!!.replace("/", "%2F")
            if (Title!!.text.toString().isEmpty() || Description!!.text.toString().isEmpty() || ur!!.isEmpty() || experience!!.text.toString().isEmpty() || mTagContainerLayout!!.tags.size == 0) {
                Toast.makeText(activity!!.applicationContext, "You must add all!", Toast.LENGTH_LONG).show()
            } else {
                println(mTagContainerLayout!!.tags)
                url += Title!!.text.toString() + "/" + Description!!.text + "/" + ur + "/" + content_type!!.selectedItem + "/" + experience!!.text + "/" + mTagContainerLayout!!.tags + "/" + user!!.id
                println(url)
                val getData = StringRequest(Request.Method.GET, url, Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getInt("success") == 1) {
                            Toast.makeText(activity!!.applicationContext, "Ligne inserré !", Toast.LENGTH_LONG).show()
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
        }
        return view
    }

    protected fun onCreateDialog(id: Int): Dialog? {
        // TODO Auto-generated method stub
        return if (id == 999) {
            DatePickerDialog(activity,
                    myDateListener, year, month, day)
        } else null
    }

    private val myDateListener = DatePickerDialog.OnDateSetListener { arg0, arg1, arg2, arg3 -> // TODO Auto-generated method stub

        showDate(arg1, arg2 + 1, arg3)
    }

    private fun showDate(year: Int, month: Int, day: Int) {
        date_begin!!.setText(StringBuilder().append(day).append("/")
                .append(month).append("/").append(year))
    }
}