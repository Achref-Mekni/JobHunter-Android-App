package com.esprit.jobhunter

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.LoginActivity
import com.esprit.jobhunter.MyUtils.GlobalParams
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class ApplicantSignupFragment : Fragment() {
    var name: EditText? = null
    var last_name: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var register: Button? = null
    var url: String? = null
    var queue: RequestQueue? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_applicant_signup, container, false)
        name = v.findViewById(R.id.nameEditText)
        last_name = v.findViewById(R.id.lastNameEditText)
        email = v.findViewById(R.id.emailEditText)
        password = v.findViewById(R.id.passwordEditText)
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        register = v.findViewById(R.id.applicationSigninButton)
        register!!.setOnClickListener(View.OnClickListener {
            if (name!!.getText().toString().equals("", ignoreCase = true) || last_name!!.getText().toString().equals("", ignoreCase = true)
                    || email!!.getText().toString().equals("", ignoreCase = true) || password!!.getText().toString().equals("", ignoreCase = true)) {
                val toast = Toast.makeText(activity!!.applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                url = GlobalParams.url + "/signupapplicant/"
                url += email!!.getText().toString() + "/" + password!!.getText() + "/" + name!!.getText() + "/" + last_name!!.getText()
                val getData = StringRequest(Request.Method.GET, url, Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getInt("success") == 1) {
                            val login = Intent(activity!!.applicationContext, LoginActivity::class.java)
                            startActivity(login)
                            activity!!.finish()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    error.printStackTrace()
                })

             queue!!.add(getData)
            }
        })

        // Inflate the layout for this fragment
        return v
    }

    companion object {
        fun newInstance(): ApplicantSignupFragment {
            return ApplicantSignupFragment()
        }
    }
}