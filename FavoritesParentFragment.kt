package com.esprit.jobhunter.MenuFragments

import android.os.Bundle
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esprit.jobhunter.PagerConfig.PageFavAdapter
import com.esprit.jobhunter.R

/**
 * A simple [Fragment] subclass.
 */
class FavoritesParentFragment : Fragment() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var pageAdapter: PageFavAdapter? = null
    var tabJobs: TabItem? = null
    var tabInternships: TabItem? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorites_parent, container, false)
        tabLayout = view.findViewById(R.id.tablayout)

        viewPager = view.findViewById(R.id.viewPager)

        pageAdapter = PageFavAdapter(this.childFragmentManager, 2)

        viewPager!!.setAdapter(pageAdapter)
        tabLayout!!.setupWithViewPager(viewPager)
        return view
    }
}