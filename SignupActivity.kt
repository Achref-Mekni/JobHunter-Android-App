package com.esprit.jobhunter

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import info.hoang8f.android.segmented.SegmentedGroup

class SignupActivity : AppCompatActivity() {
    //UI Declaration
    var userTypeSeg: SegmentedGroup? = null

    //Global var Declaration
    private var isApplicantFragmentDisplayed = false
    private var isCompanyFragmentDisplayed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        userTypeSeg = findViewById<View>(R.id.userTypeSeg) as SegmentedGroup
        userTypeSeg!!.setTintColor(Color.parseColor("#FFFFFF"), Color.parseColor("#373447"))
        userTypeSeg!!.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.normalUserSeg -> applicantFrag()
                R.id.companySeg -> companyFrag()
                else -> {
                }
            }
        }
    }

    fun applicantFrag() {
        if (isApplicantFragmentDisplayed == false) {
            closeCompanyFrag()
            displayApplicantFrag()
        } else {
            closeApplicantFrag()
        }
    }

    fun companyFrag() {
        if (isCompanyFragmentDisplayed == false) {
            closeApplicantFrag()
            displayCompanyFrag()
        } else {
            closeCompanyFrag()
        }
    }

    fun displayApplicantFrag() {
        val applicantSignupFragment = ApplicantSignupFragment.newInstance()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.signupFragContainer, applicantSignupFragment).commit()
        isApplicantFragmentDisplayed = true
    }

    fun displayCompanyFrag() {
        val companySignupFragment = CompanySignupFragment.newInstance()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.signupFragContainer, companySignupFragment).commit()
        isCompanyFragmentDisplayed = true
    }

    fun closeApplicantFrag() {
        val fragmentManager = supportFragmentManager
        val applicantSignupFragment = fragmentManager.findFragmentById(R.id.signupFragContainer) as ApplicantSignupFragment?
        if (applicantSignupFragment != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(applicantSignupFragment).commit()
        }
        isApplicantFragmentDisplayed = false
    }

    fun closeCompanyFrag() {
        val fragmentManager = supportFragmentManager
        val companySignupFragment = fragmentManager.findFragmentById(R.id.signupFragContainer) as CompanySignupFragment?
        if (companySignupFragment != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(companySignupFragment).commit()
        }
        isCompanyFragmentDisplayed = false
    }
}