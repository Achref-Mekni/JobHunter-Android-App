package com.esprit.jobhunter.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.esprit.jobhunter.CompanyFragments.ListinternshipsFragment
import com.esprit.jobhunter.CompanyFragments.ListoffersFragment

class PagerAdapter(fm: FragmentManager?, private val numOfTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return ListoffersFragment()
            1 -> return ListinternshipsFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return numOfTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Jobs"
            1 -> return "Internships"
        }
        return null
    }

}