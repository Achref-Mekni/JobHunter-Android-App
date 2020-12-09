package com.esprit.jobhunter.MenuFragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esprit.jobhunter.PagerConfig.PageApplicationsAdapter
import com.esprit.jobhunter.R

/**
 * A simple [Fragment] subclass.
 */
class ApplicationsFragment : Fragment() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var pageAdapter: PageApplicationsAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_applications, container, false)
        tabLayout = view.findViewById(R.id.tablayout)
        viewPager = view.findViewById(R.id.viewPager)
        pageAdapter = PageApplicationsAdapter(this.childFragmentManager, 2)
        viewPager!!.setAdapter(pageAdapter)
        tabLayout!!.setupWithViewPager(viewPager)
        return view
    }

    companion object {
        fun newInstance(): ApplicationsFragment {
            return ApplicationsFragment()
        }
    }
}