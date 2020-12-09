package com.esprit.jobhunter.CompanyFragments

import android.os.Bundle
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esprit.jobhunter.Adapters.PagerAddAdapter
import com.esprit.jobhunter.R

class AddOffersFragment : Fragment() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var pageAdapter: PagerAddAdapter? = null
    var tabJobs: TabItem? = null
    var tabInternships: TabItem? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)
        tabLayout = view.findViewById(R.id.tablayout)

        viewPager = view.findViewById(R.id.viewPager)

        pageAdapter = PagerAddAdapter(this.childFragmentManager, 2)

        viewPager!!.setAdapter(pageAdapter)
        tabLayout!!.setupWithViewPager(viewPager)
        return view

    }

    companion object {
        fun newInstance(): AddOffersFragment {
            return AddOffersFragment()
        }
    }
}