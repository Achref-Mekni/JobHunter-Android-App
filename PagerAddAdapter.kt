package com.esprit.jobhunter.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.esprit.jobhunter.CompanyFragments.AddoffreFragment
import com.esprit.jobhunter.CompanyFragments.AddoffreinternshipFragment

class PagerAddAdapter(fm: FragmentManager?, private val numOfTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return AddoffreFragment()
            1 -> return AddoffreinternshipFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return numOfTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Job"
            1 -> return "Internship"
        }
        return null
    }

}