package com.abhi.food_app.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.abhi.food_app.R
import com.abhi.food_app.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etMobile: EditText
    lateinit var etAddress: EditText
    lateinit var etEmailId: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var toolbar: Toolbar
    lateinit var tvLogin: TextView
    lateinit var btSignup: Button
    lateinit var progresslayout: RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etMobile = findViewById(R.id.etPhone)
        etEmailId = findViewById(R.id.etEmailId)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btSignup = findViewById(R.id.btSignup)
        toolbar = findViewById(R.id.toolbar)
        tvLogin = findViewById(R.id.tvlogin)
        progresslayout = findViewById(R.id.register_fragment_Progressdialog)

        setToolBar()

        btSignup.setOnClickListener {
            registerUserFun()

        }


    }

    fun userSuccessfullyRegistered() {
        openDashBoard()
    }

    fun openDashBoard() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish();

    }

    fun registerUserFun() {


        val sharedPreferencess = this.getSharedPreferences(
            getString(R.string.register_preference_file),
            Context.MODE_PRIVATE
        )


        sharedPreferencess.edit().putBoolean("user_logged_in", false).apply()

        if (ConnectionManager().checkConnectivity(this)) {


            if (checkForErrors()) {

                progresslayout.visibility = View.VISIBLE
                try {

                    val registerUser = JSONObject()
                    registerUser.put("name", etName.text)
                    registerUser.put("mobile_number", etMobile.text)
                    registerUser.put("password", etPassword.text)
                    registerUser.put("address", etAddress.text)
                    registerUser.put("email", etEmailId.text)


                    val queue = Volley.newRequestQueue(this)

                    val url =
                        "http://13.235.250.119/v2/register/fetch_result"

                    val jsonObjectRequest = object : JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        registerUser,
                        Response.Listener {
                            println("Response12 is " + it)

                            val responseJsonObjectData = it.getJSONObject("data")

                            val success = responseJsonObjectData.getBoolean("success")


                            if (!success) {

                                val data = responseJsonObjectData.getJSONObject("data")
                                sharedPreferencess.edit().putBoolean("user_logged_in", true)
                                    .apply()
                                sharedPreferencess.edit()
                                    .putString("user_id", data.getString("user_id")).apply()
                                sharedPreferencess.edit().putString("name", data.getString("name"))
                                    .apply()
                                sharedPreferencess.edit()
                                    .putString("email", data.getString("email")).apply()
                                sharedPreferencess.edit()
                                    .putString("mobile_number", data.getString("mobile_number"))
                                    .apply()
                                sharedPreferencess.edit()
                                    .putString("address", data.getString("address")).apply()


                                Toast.makeText(
                                    this,
                                    "Registered sucessfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                userSuccessfullyRegistered()//after we get a response we call the login


                            } else {
                                val responseMessageServer =
                                    responseJsonObjectData.getString("errorMessage")
                                Toast.makeText(
                                    this,
                                    success.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            progresslayout.visibility = View.INVISIBLE
                        },
                        Response.ErrorListener {
                            println("Error12 is " + it)
                            progresslayout.visibility = View.INVISIBLE
                            println(it)
                            Toast.makeText(
                                this,
                                "Some Error occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()


                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()

                            headers["Content-type"] = "application/json"
                            headers["token"] = "12fed3f7359e2"

                            return headers
                        }
                    }

                    queue.add(jsonObjectRequest)

                } catch (e: JSONException) {
                    Toast.makeText(
                        this,
                        "Some unexpected error occured!!!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

        } else {
            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)

            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)

            }

            alterDialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this)//closes all the instances of the app and the app closes completely
            }
            alterDialog.create()
            alterDialog.show()

        }
    }

    fun checkForErrors(): Boolean {
        var errorPassCount = 0
        if (etName.text.isBlank()) {

            etName.setError("Field Missing!")
        } else {
            errorPassCount++
        }

        if (etMobile.text.isBlank()) {
            etMobile.setError("Field Missing!")
        } else {
            errorPassCount++
        }

        if (etEmailId.text.isBlank()) {
            etEmailId.setError("Field Missing!")
        } else {
            errorPassCount++
        }

        if (etAddress.text.isBlank()) {
            etAddress.setError("Field Missing!")
        } else {
            errorPassCount++
        }

        if (etConfirmPassword.text.isBlank()) {
            etConfirmPassword.setError("Field Missing!")
        } else {
            errorPassCount++
        }

        if (etPassword.text.isBlank()) {
            etPassword.setError("Field Missing!")
        } else {
            errorPassCount++
        }

        if (etPassword.text.isNotBlank() && etConfirmPassword.text.isNotBlank()) {
            if (etPassword.text.toString().toInt() == etConfirmPassword.text.toString()
                    .toInt()
            ) {
                errorPassCount++
            } else {
                etConfirmPassword.setError("Password don't match")
            }
        }

        if (errorPassCount == 7)
            return true
        else
            return false

    }

    fun setToolBar() {

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)//enables the button on the tool bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//displays the icon on the button
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)//change icon to custom
    }


    override fun onResume() {

        if (!ConnectionManager().checkConnectivity(this)) {

            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            dialog.setTitle("No Internet")
            dialog.setMessage("Internet Connection can't be establish!")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)
            }

            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this)//closes all the instances of the app and the app closes completely
            }
            dialog.setCancelable(false)

            dialog.create()
            dialog.show()

        }

        super.onResume()
    }


}
