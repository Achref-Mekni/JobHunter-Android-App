package com.esprit.jobhunter.PagerConfig

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.esprit.jobhunter.OffersFragments.OffersJobsFragment
import com.esprit.jobhunter.OffersFragments.OffersInternshipsFragment

class ViewPagerAdapter // Build a Constructor and assign the passed Values to appropriate values in the class
(fm: FragmentManager?, // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
 var Titles: Array<CharSequence>, // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
 var NumbOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    //This method return the fragment for the every position in the View Pager
    override fun getItem(position: Int): Fragment {
        return if (position == 0) // if the position is 0 we are returning the First tab
        {
            OffersJobsFragment()
        } else  // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            OffersInternshipsFragment()
        }
    }

    // This method return the titles for the Tabs in the Tab Strip
    override fun getPageTitle(position: Int): CharSequence? {
        return Titles[position]
    }

    // This method return the Number of tabs for the tabs Strip
    override fun getCount(): Int {
        return NumbOfTabs
    }

}