package com.esprit.jobhunter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.LoginActivity
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.util.function.Consumer

class LoginActivity : AppCompatActivity(), View.OnClickListener, OnConnectionFailedListener {
    var url: String? = null
    var queue: RequestQueue? = null
    var user: User? = null
    var user1: User? = null
    var count = 0
    var cd_id_linkedin = 0

    //UI Declarations
    var signupBtn: Button? = null
    var signinBtn: Button? = null

    private var remember_me: CheckBox? = null
    private var loginPreferences: SharedPreferences? = null
    private var loginPrefsEditor: SharedPreferences.Editor? = null
    private var saveLogin: Boolean? = null
    var login_layout: LinearLayout? = null
    var loginProgress: ProgressBar? = null
    var viewmore: Button? = null
    var viewmore2: Button? = null
    var imgLogin: SignInButton? = null
    var login_layout2: LinearLayout? = null
    var imgProfil: ImageView? = null
    var password: EditText? = null
    var login: EditText? = null
    var login_text: String? = null
    var password_text: String? = null
    var Name: String? = null

    /***** added new  */
    private var googleApiClient: GoogleApiClient? = null

    /** */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        computePakageHash()
        loginProgress = findViewById<View>(R.id.progressBarLogin) as ProgressBar
        loginProgress!!.visibility = View.GONE
        signupBtn = findViewById<View>(R.id.signupButton) as Button
        signinBtn = findViewById<View>(R.id.signinButton) as Button
        login_layout = findViewById<View>(R.id.login_layout) as LinearLayout
        login_layout2 = findViewById<View>(R.id.login_layout2) as LinearLayout
        signupBtn!!.setOnClickListener(signupBtnOnClickListener)
        signinBtn!!.setOnClickListener(signinBtnOnClickListener)
        /***** added new  */
        handler.postDelayed(runnable, 2000)
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build()
        /** */
        login = findViewById(R.id.emailInput)
        password = findViewById(R.id.passwordInput)
        user = User()

        //Remember_me
        remember_me = findViewById<View>(R.id.remember_me) as CheckBox
        loginPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        loginPrefsEditor = loginPreferences!!.edit()
        saveLogin = loginPreferences!!.getBoolean("saveLogin", false)
        if (saveLogin == true) {
            login!!.setText(loginPreferences!!.getString("username", ""))
            password!!.setText(loginPreferences!!.getString("password", ""))
            remember_me!!.isChecked = true
        }
        initializeControls()
    }

    private val signupBtnOnClickListener = View.OnClickListener { signupBtnClicked() }

    private fun signupBtnClicked() {
        val signupIntent = Intent(applicationContext, SignupActivity::class.java)
        startActivity(signupIntent)
    }

    //--------------------------
    private val signinBtnOnClickListener = View.OnClickListener { signinBtnClicked() }

    //------------------------- LINKEDIN SDK
    private fun initializeControls() {
        imgLogin = findViewById<View>(R.id.imgLogin) as SignInButton
        imgLogin!!.setOnClickListener(this)
    }

    private fun computePakageHash() {
        try {
            val info = packageManager.getPackageInfo(
                    "com.esprit.jobhunter",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: Exception) {
            Log.e("TAG", e.message)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgLogin -> handleLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("amir1", "aaaaa")
        if (requestCode == REQ_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            fetchPersonalInfo(result, Consumer { v: User ->
                checkuser(v, Consumer<Any> { t: Any? ->
                    if (count == 0) adduser(v) else {
                        getFullUser(user1)
                    }
                })
            })
            login_layout!!.alpha = 0f
        }
    }

    private fun handleLogin() {
        val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(intent, REQ_CODE)
    }

    private fun fetchPersonalInfo(result: GoogleSignInResult, consumer: Consumer<User>) {
        Log.d("amir", "aaaaa")
        if (result.isSuccess) {
            user1 = User()
            val account = result.signInAccount
            val fname = account!!.displayName
            val space = fname!!.indexOf(" ")
            user1!!.name = fname.substring(0, space)
            user1!!.last_name = account.familyName
            user1!!.email = account.email
            if (account.photoUrl == null) {
                user1!!.picture = "profile-default.png"
            } else {
                var ur = account.photoUrl.toString()
                ur = ur.replace("/", "%2F")
                user1!!.picture = ur
            }

            println("user1$user1")
            consumer.accept(user1!!)
        } else {
            Log.d("amir3", "nope")
        }
    }

    private fun checkuser(user1: User, consumer: Consumer<Any>) {
        queue = Volley.newRequestQueue(this)
        url = GlobalParams.url + "/getuserbyemail/"
        url += user1.email
        println(url)
        val getData = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                count = 0
                val obj = JSONObject(response)
                val row = JSONArray(obj.getString("result"))
                val use = row.getJSONObject(0)
                count = use.getInt("count(email)")
                consumer.accept(count)
            } catch (e: JSONException) {
                Log.e("JSONExeption", e.message)
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()
        })
        queue!!.add(getData)
    }

    private fun adduser(user1: User) {
        var url1 = GlobalParams.url + "/signupapplicant_linkedin/"
        println("piccccccccccccccccccccccc" + user1.picture)
        url1 += user1.email + "/" + user1.password + "/" + user1.name + "/" + user1.last_name + "/" + user1.picture
        println(url1)
        val getData = StringRequest(Request.Method.GET, url1, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                if (obj.getInt("success") == 1) {
                }
            } catch (e: JSONException) {
                Log.e("JSONExeption", e.message)
            }
        }, Response.ErrorListener { error -> Log.e("ErrorResponse", error.message) })
        //--------------
        val getFullUser = StringRequest(Request.Method.GET, GlobalParams.url + "/getuserlikedin/" + user1.email, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                if (obj.getInt("success") == 1) {
                    val row = JSONArray(obj.getString("result"))
                    val use = row.getJSONObject(0)
                    user1.id = use.getInt("id")
                    user1.name = use.getString("name")
                    user1.last_name = use.getString("last_name")
                    user1.birth_date = use.getString("birth_date")
                    user1.gender = use.getString("gender")
                    user1.adress = use.getString("adress")
                    user1.tel1 = use.getString("tel1")
                    user1.tel2 = use.getString("tel2")
                    user1.fax = use.getString("fax")
                    user1.nationality = use.getString("nationality")
                    user1.description = use.getString("description")
                    user1.picture = use.getString("picture")
                    user1.type = use.getString("type")
                    user1.cv_id = use.getString("cv_id")
                }
                val home = Intent(applicationContext, MainActivity::class.java)
                home.putExtra("connectedUser", user1)
                startActivity(home)
                finish()
            } catch (e: JSONException) {
                Log.e("JSONExeption", e.message)
            }
        }, Response.ErrorListener { error -> Log.e("ErrorResponse", error.message) })
        //--------------
        queue!!.add(getData)
        queue!!.add(getFullUser)
    }

    //-------------------------
    private fun signinBtnClicked() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(password!!.windowToken, 0)
        login_text = login!!.text.toString()
        password_text = password!!.text.toString()
        if (remember_me!!.isChecked) {
            loginPrefsEditor!!.putBoolean("saveLogin", true)
            loginPrefsEditor!!.putString("username", login_text)
            loginPrefsEditor!!.putString("password", password_text)
            loginPrefsEditor!!.commit()
        } else {
            loginPrefsEditor!!.clear()
            loginPrefsEditor!!.commit()
        }
        //----------------------------
        loginProgress!!.visibility = View.VISIBLE
        queue = Volley.newRequestQueue(this)
        url = GlobalParams.url + "/login/"
        if (login!!.text.length != 0 && password!!.text.length != 0) {
            url += login!!.text.toString() + "/" + password!!.text
            val getData = StringRequest(Request.Method.GET, url, Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("success") == 1) {
                        val row = JSONArray(obj.getString("result"))
                        val use = row.getJSONObject(0)
                        user!!.id = use.getInt("id")
                        user!!.name = use.getString("name")
                        user!!.last_name = use.getString("last_name")
                        user!!.birth_date = use.getString("birth_date")
                        user!!.gender = use.getString("gender")
                        user!!.email = use.getString("email")
                        user!!.adress = use.getString("adress")
                        user!!.tel1 = use.getString("tel1")
                        user!!.tel2 = use.getString("tel2")
                        user!!.fax = use.getString("fax")
                        user!!.nationality = use.getString("nationality")
                        user!!.description = use.getString("description")
                        user!!.picture = use.getString("picture")
                        user!!.type = use.getString("type")
                        user!!.cv_id = use.getString("cv_id")

                        if (user!!.type == "a") {
                            val home = Intent(applicationContext, MainActivity::class.java)
                            home.putExtra("connectedUser", user)
                            startActivity(home)
                            finish()
                        } else if (user!!.type == "c") {
                            val home = Intent(applicationContext, Main2Activity::class.java)
                            home.putExtra("connectedUser", user)
                            startActivity(home)
                            finish()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Invalid password or login", Toast.LENGTH_LONG).show()
                        login_layout!!.alpha = 1f
                    }
                    loginProgress!!.visibility = View.GONE
                    login_layout!!.alpha = 1f
                } catch (e: JSONException) {
                    Log.e("JSONExeption", e.message)
                }
            }, Response.ErrorListener { error ->
                val alertDialog = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme).create()
                alertDialog.setTitle("Error !")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                if (error is NetworkError) {
                    alertDialog.setMessage("Cannot connect to Internet...")
                    alertDialog.show()
                    loginProgress!!.visibility = View.GONE
                } else if (error is ServerError) {
                    alertDialog.setMessage("The server could not be found...")
                    alertDialog.show()
                    loginProgress!!.visibility = View.GONE
                } else if (error is AuthFailureError) {
                    alertDialog.setMessage("Cannot connect to Internet...")
                    alertDialog.show()
                    loginProgress!!.visibility = View.GONE
                } else if (error is ParseError) {
                    alertDialog.setMessage("Parsing error...")
                    alertDialog.show()
                    loginProgress!!.visibility = View.GONE
                } else if (error is NoConnectionError) {
                    alertDialog.setMessage("Cannot connect to Internet...")
                    alertDialog.show()
                    loginProgress!!.visibility = View.GONE
                } else if (error is TimeoutError) {
                    alertDialog.setMessage("Connection TimeOut...")
                    alertDialog.show()
                    loginProgress!!.visibility = View.GONE
                }
            })
            queue!!.add(getData)
        }


    }

    fun getFullUser(user1: User?) {
        queue = Volley.newRequestQueue(this)
        val getFullUser = StringRequest(Request.Method.GET, GlobalParams.url + "/getuserlikedin/" + user1!!.email, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                if (obj.getInt("success") == 1) {
                    val row = JSONArray(obj.getString("result"))
                    val use = row.getJSONObject(0)
                    user1.id = use.getInt("id")
                    user1.name = use.getString("name")
                    user1.last_name = use.getString("last_name")
                    user1.birth_date = use.getString("birth_date")
                    user1.gender = use.getString("gender")
                    user1.adress = use.getString("adress")
                    user1.tel1 = use.getString("tel1")
                    user1.tel2 = use.getString("tel2")
                    user1.fax = use.getString("fax")
                    user1.nationality = use.getString("nationality")
                    user1.description = use.getString("description")
                    user1.picture = use.getString("picture")
                    user1.type = use.getString("type")
                    user1.cv_id = use.getString("cv_id")
                }
                val home = Intent(applicationContext, MainActivity::class.java)
                home.putExtra("connectedUser", user1)
                startActivity(home)
                finish()
            } catch (e: JSONException) {
                Log.e("JSONExeption", e.message)
            }
        }, Response.ErrorListener { error -> Log.e("ErrorResponse", error.message) })
        queue!!.add(getFullUser)
    }

    var handler = Handler()
    var runnable = Runnable {
        login_layout!!.visibility = View.VISIBLE
        login_layout2!!.visibility = View.VISIBLE
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    companion object {
        private const val REQ_CODE = 9001
    }
}