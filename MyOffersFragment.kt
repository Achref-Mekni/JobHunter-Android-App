package com.esprit.jobhunter.CompanyFragments

import android.os.Bundle
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esprit.jobhunter.Adapters.PagerAdapter
import com.esprit.jobhunter.R

class MyOffersFragment : Fragment() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var pageAdapter: PagerAdapter? = null
    var tabJobs: TabItem? = null
    var tabInternships: TabItem? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)
        tabLayout = view.findViewById(R.id.tablayout)
        viewPager = view.findViewById(R.id.viewPager)

        pageAdapter = PagerAdapter(this.childFragmentManager, 2)

        viewPager!!.setAdapter(pageAdapter)
        tabLayout!!.setupWithViewPager(viewPager)


        // Inflate the layout for this fragment
        return view
    }

    companion object {
        fun newInstance(): MyOffersFragment {
            return MyOffersFragment()
        }
    }
}