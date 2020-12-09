package com.esprit.jobhunter.CompanyFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.Main2Activity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 */
class ProfilCompanyFragment : Fragment() {
    var tvName: TextView? = null
    var tvAddress: TextView? = null
    var email: TextView? = null
    var contact: TextView? = null
    var description: TextView? = null
    var civProfilePic: ImageView? = null
    var user: User? = null
    var test1 = ""
    var test2 = ""
    var test3 = ""
    var queue: RequestQueue? = null
    private var progressBarProfile: ProgressBar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profil_company, container, false)
        progressBarProfile = view.findViewById<View>(R.id.progressBarProfil) as ProgressBar
        progressBarProfile!!.visibility = View.VISIBLE
        val photoHeader = view.findViewById<View>(R.id.photoHeader)
        photoHeader.translationZ = 6f
        photoHeader.invalidate()
        tvName = view.findViewById<View>(R.id.tvName) as TextView
        tvAddress = view.findViewById<View>(R.id.tvAddress) as TextView
        email = view.findViewById<View>(R.id.email) as TextView
        contact = view.findViewById<View>(R.id.contact) as TextView
        description = view.findViewById<View>(R.id.description) as TextView
        civProfilePic = view.findViewById<View>(R.id.civProfilePic) as ImageView
        queue = Volley.newRequestQueue(activity!!.applicationContext)
        user = User()
        user = (activity as Main2Activity?)!!.connectedUser
        tvName!!.text = user!!.name
        tvAddress!!.text = user!!.adress
        email!!.text = user!!.email
        if (user!!.description != null && user!!.description != "null") {
            description!!.text = user!!.description
        } else {
            description!!.text = ""
        }
        var cont = ""
        if (user!!.tel1 != null && user!!.tel1 != "null") {
            test1 = "Tel1: " + user!!.tel1
        }
        if (user!!.tel2 != null && user!!.tel2 != "null") {
            test2 = "Tel2: " + user!!.tel2
        }
        if (user!!.fax != null && user!!.fax != "null") {
            test3 = "Fax: " + user!!.fax
        }
        if (test1.isEmpty() && test2.isEmpty() && test3.isEmpty()) cont = cont + ""
        if (!test1.isEmpty() && !test2.isEmpty() && !test3.isEmpty()) cont = "$cont$test1 - $test2 - $test3"
        if (!test1.isEmpty() && test2.isEmpty() && test3.isEmpty()) cont = cont + test1
        if (test1.isEmpty() && test2.isEmpty() && !test3.isEmpty()) cont = cont + test3
        if (test1.isEmpty() && !test2.isEmpty() && test3.isEmpty()) cont = cont + test2
        if (!test1.isEmpty() && !test2.isEmpty() && test3.isEmpty()) cont = "$cont$test1 - $test2"
        if (test1.isEmpty() && !test2.isEmpty() && !test3.isEmpty()) cont = "$cont$test2 - $test3"
        if (!test1.isEmpty() && test2.isEmpty() && !test3.isEmpty()) cont = "$cont$test1 - $test3"
        contact!!.text = cont
        if (user!!.picture != null) {

            if (user!!.picture!!.length > 60) {
                Picasso.with(activity!!.applicationContext).load(user!!.picture).into(civProfilePic)
            } else {
                Picasso.with(activity!!.applicationContext).load(GlobalParams.ressourceUrl + "/" + user!!.picture).into(civProfilePic)
            }
        }
        progressBarProfile!!.visibility = View.GONE
        return view
    }
}