package com.esprit.jobhunter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.SearchAutoComplete
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Adapters.SearchAdapter
import com.esprit.jobhunter.Entity.Job
import com.esprit.jobhunter.Entity.Offer
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MenuFragments.*
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.OffersFragments.JobDetailsFragment
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.function.BiConsumer

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var drawer: DrawerLayout? = null
    var searchView: SearchView? = null

    //----------------------
    var connectedUser: User? = null
    private var fragment: Fragment? = null
    private var fragmentManager: FragmentManager? = null

    //------Linkedin vars
    var NameLastName: TextView? = null
    var f = false
    var imgProfil: ImageView? = null

    //------
    //SearchView Vars
    private val JsonString = ""
    private var offersList: ArrayList<Offer>? = null
    var list: ArrayList<Offer>? = null
    var jobToSend: Job? = null
    var x: Job? = null

    //-----------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //-----------SearchView inits
        offersList = ArrayList()

        //---------------Getting connected user
        val fromLogin = intent
        connectedUser = fromLogin.getSerializableExtra("connectedUser") as User
        //----------------
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // toolbar fancy stuff
        supportActionBar!!.setTitle("Search")

        // Get ActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
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
            navUsername.text = connectedUser!!.name + " " + connectedUser!!.last_name
            connectedUser!!.picture = connectedUser!!.picture!!.replace("%2F", "/")
            imgProfil = headerView.findViewById<View>(R.id.header_imageView) as ImageView

           if (connectedUser!!.picture!!.length > 60) {
                Picasso.with(this.applicationContext).load(connectedUser!!.picture).into(imgProfil)
            } else {
                Picasso.with(this.applicationContext).load(GlobalParams.ressourceUrl + "/" + connectedUser!!.picture).into(imgProfil)
            }
        }

        //----------------------
        fragment = OffersFragment()
        navigationView.setCheckedItem(R.id.nav_offers)
        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.flContent, fragment!!)
        tx.commit()
    }

    //-------------------------------------
    private fun getJobById(id: Int, consumer: BiConsumer<Job, Nullable?>) {
        x = Job()
        val queue = Volley.newRequestQueue(applicationContext)
        val getData = StringRequest(Request.Method.GET, GlobalParams.url + "/getjobbyid/" + id, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                if (obj.getInt("success") == 1) {
                    val row = JSONArray(obj.getString("result"))
                    val use = row.getJSONObject(0)
                    val job = Job()
                    job.id = use.getInt("id")
                    job.label = use.getString("label")
                    job.description = use.getString("description")
                    job.start_date = use.getString("start_date")
                    job.contract_type = use.getString("contract_type")
                    job.career_req = use.getString("career_req")
                    job.user_id = use.getString("user_id")
                    job.company_name = use.getString("name")
                    job.company_pic = use.getString("picture")
                    job.skills = use.getString("skills")
                    consumer.accept(job, null)
                    x = job
                }
            } catch (e: JSONException) {
                Log.e("JSONExeption", e.message)
            }
            jobToSend = x
            println("UUUUUUUUUUUUUUUUUU: $jobToSend")
        }, Response.ErrorListener { error ->
            error.printStackTrace()
        })
        queue.add(getData)
    }

    //-------------------------------------
    private fun fillOffers(consumer: BiConsumer<List<Offer>, Nullable?>) {
        val queue = Volley.newRequestQueue(applicationContext)
        val jsonArrayRequest2 = JsonObjectRequest(
                Request.Method.GET,
                GlobalParams.url + "/internships",
                null,
                Response.Listener { response ->
                    var jsonArray: JSONArray? = null
                    val list: MutableList<Offer> = ArrayList()
                    try {
                        jsonArray = response.getJSONArray("result")
                        for (i in 0 until jsonArray.length()) {
                            try {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val internship = Offer()
                                internship.id = jsonObject.getInt("id")
                                internship.label = jsonObject.getString("label")
                                internship.cmp_name = jsonObject.getString("name")
                                internship.cmp_pic = jsonObject.getString("picture")
                                internship.type = "i"
                                list.add(internship)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        consumer.accept(list, null)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    println("LLLLLLLLLLL:  $offersList")
                },
                Response.ErrorListener { error -> Log.e("ERROR", error.message) }
        )
        queue.add(jsonArrayRequest2)
        //----
    }

    private fun fillOffers2(consumer: BiConsumer<List<Offer>, Nullable?>) {
        val queue = Volley.newRequestQueue(applicationContext)
        val jsonArrayRequest = JsonObjectRequest(
                Request.Method.GET,
                GlobalParams.url + "/jobs",
                null,
                Response.Listener { response ->
                    var jsonArray: JSONArray? = null
                    val list: MutableList<Offer> = ArrayList()
                    try {
                        jsonArray = response.getJSONArray("result")
                        for (i in 0 until jsonArray.length()) {
                            try {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val job = Offer()
                                job.id = jsonObject.getInt("id")
                                job.label = jsonObject.getString("label")
                                job.cmp_name = jsonObject.getString("name")
                                job.cmp_pic = jsonObject.getString("picture")
                                job.type = "j"
                                list.add(job)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        consumer.accept(list, null)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Log.e("ERROR", error.message) }
        )
        queue.add(jsonArrayRequest)
    }

    //-------------------------------------

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        // Inflate the search menu action bar.
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.action_bar_search_example_menu, menu)

        // Get the search menu.
        val searchMenu = menu.findItem(R.id.app_bar_menu_search)

        // Get SearchView object.
        val searchView = MenuItemCompat.getActionView(searchMenu) as SearchView

        // Get SearchView autocomplete object.
        val searchAutoComplete = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as SearchAutoComplete
        searchAutoComplete.setTextColor(Color.BLACK)

        // Create a new ArrayAdapter and add data to search auto complete object.

        //------------------------------------------
        println("MMMMMMMMMMMMMMMMMMMMMM:  $offersList")
        fillOffers(BiConsumer { data: List<Offer>, n: Nullable? ->
            fillOffers2(BiConsumer { data2: List<Offer>?, n2: Nullable? ->
                var new = data !! +data2!!
                val searchAdapter = SearchAdapter(this, R.layout.search_row, new)
                searchAutoComplete.setAdapter(searchAdapter)
                searchAutoComplete.threshold = 1
            })
        })

        // Listen to search view item on click event.
        searchAutoComplete.onItemClickListener = OnItemClickListener { adapterView, view, itemIndex, id ->

            val offer = adapterView.getItemAtPosition(itemIndex) as Offer
            searchAutoComplete.setText(offer.label)
            //--------
            if (offer.type == "j") {
                getJobById(offer.id, BiConsumer { data: Job?, n: Nullable? ->
                    val fragment: Fragment = JobDetailsFragment()
                    (fragment as JobDetailsFragment).setData(data!!)
                    supportFragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
                })
            } else if (offer.type == "i") {
            }

            //--------
        }

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })




        return super.onCreateOptionsMenu(menu)
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
        } else super.onBackPressed()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //-------------------------
        var fragment: Fragment? = null
        //Class fragmentClass;
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_offers) {
            fragment = OffersFragment()
        } else if (id == R.id.nav_favorites) {
            fragment = FavoritesFragment()
        } else if (id == R.id.nav_applications) {
            fragment = ApplicationsFragment()
        } else if (id == R.id.nav_contacts) {
            fragment = ContactsFragment()
        } else if (id == R.id.nav_profile) {
            fragment = ProfileFragment()
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