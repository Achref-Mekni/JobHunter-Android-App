package com.esprit.jobhunter

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.esprit.jobhunter.CompanyFragments.AddOffersFragment
import com.esprit.jobhunter.CompanyFragments.CompanyContactFragment
import com.esprit.jobhunter.CompanyFragments.MyOffersFragment
import com.esprit.jobhunter.CompanyFragments.ProfilCompanyFragment
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.squareup.picasso.Picasso

class Main2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var drawer: DrawerLayout? = null
    var searchView: SearchView? = null
    var connectedUser: User? = null
    private var fragment: Fragment? = null
    private var fragmentManager: FragmentManager? = null

    //------Linkedin vars
    var NameLastName: TextView? = null
    var f = false
    var imgProfil: ImageView? = null

    //------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        //----------------Getting connected user
        val fromLogin = intent
        connectedUser = fromLogin.getSerializableExtra("connectedUser") as User
        //----------------
        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)

        // toolbar fancy stuff
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Search")
        //-------

        //-------

        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        //-------------set Header name/image

        if (intent.extras.containsKey("connectedUser")) {
            val headerView = navigationView.getHeaderView(0)
            val navUsername = headerView.findViewById<View>(R.id.header_name) as TextView
            navUsername.text = connectedUser!!.name
            val navUserimage = headerView.findViewById<View>(R.id.header_imageView) as ImageView

            if (connectedUser!!.picture!!.length > 60) {
                Picasso.with(this.applicationContext).load(connectedUser!!.picture).into(navUserimage)
            } else {
                Picasso.with(this.applicationContext).load(GlobalParams.ressourceUrl + "/" + connectedUser!!.picture).into(navUserimage)
            }

        }
        //----------------------
        fragment = MyOffersFragment()
        navigationView.setCheckedItem(R.id.nav_gallery)
        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.flContent, fragment!!)
        tx.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Int.MAX_VALUE
        searchView!!.queryHint = "Search..."


        // listening to search query text change
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed

                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_search) {
            return true
        }
        return if (id == R.id.nav_offers) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else  // close search view on back button pressed
            if (!searchView!!.isIconified) {
                searchView!!.isIconified = true
                return
            } else super.onBackPressed()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        val id = item.itemId
        if (id == R.id.nav_camera) {
            fragment = AddOffersFragment()
        } else if (id == R.id.nav_gallery) {
            fragment = MyOffersFragment()
        } else if (id == R.id.nav_manage) {
            fragment = CompanyContactFragment()
        } else if (id == R.id.nav_share) {
            fragment = ProfilCompanyFragment()
        } else if (id == R.id.nav_signout) {
            val loginIntent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return true
        }
        fragmentManager = supportFragmentManager
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.flContent, fragment!!)
        transaction.commit()
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    } //----Fragments Display
}