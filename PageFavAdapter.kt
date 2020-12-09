package com.esprit.jobhunter.PagerConfig

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.esprit.jobhunter.MenuFragments.Favorites1Fragment
import com.esprit.jobhunter.MenuFragments.FavoritesFragment

class PageFavAdapter(fm: FragmentManager?, private val numOfTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return FavoritesFragment()
            1 -> return Favorites1Fragment()
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