package com.esprit.jobhunter.PagerConfig

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.esprit.jobhunter.ApplicationsFragments.ApplicationsInternshipsFragment
import com.esprit.jobhunter.ApplicationsFragments.ApplicationsJobsFragment

class PageApplicationsAdapter(fm: FragmentManager?, private val numOfTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return ApplicationsJobsFragment()
            1 -> return ApplicationsInternshipsFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return numOfTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Jobs Applications"
            1 -> return "Internships Applications"
        }
        return null
    }

}