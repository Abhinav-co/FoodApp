package com.abhi.food_app.activity

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.abhi.food_app.R
import com.abhi.food_app.fragments.HomeFragment
import com.abhi.food_app.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var etmobileNo: EditText
    lateinit var etpassword: EditText
    lateinit var btlogin: Button
    lateinit var tvforgotPassword: TextView
    lateinit var tvregister: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences =
            getSharedPreferences(getString(R.string.register_preference_file), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        etmobileNo = findViewById(R.id.mobileNo)
        etpassword = findViewById((R.id.password))
        btlogin = findViewById(R.id.logInButt)
        tvforgotPassword = findViewById(R.id.forgotpass)
        tvregister = findViewById(R.id.register)

        tvforgotPassword.paintFlags= Paint.UNDERLINE_TEXT_FLAG
        tvregister.paintFlags=Paint.UNDERLINE_TEXT_FLAG

        tvregister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
        btlogin.setOnClickListener {

            if (etmobileNo.text.isBlank()) {
                etmobileNo.error = "Mobile Number Missing"
                btlogin.visibility= View.VISIBLE
            }
            else{
                if(etpassword.text.isBlank())
                {
                    btlogin.visibility=View.VISIBLE
                    etpassword.error = "Missing Password"
                }else{
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish();
                }
            }
        }
    }



    fun loginUserFun(){

        val sharedPreferencess=getSharedPreferences(getString(R.string.register_preference_file),Context.MODE_PRIVATE)


        if (ConnectionManager().checkConnectivity(this)) {

            //login_fragment_Progressdialog.visibility=View.VISIBLE
            try {

                val loginUser = JSONObject()

                loginUser.put("mobile_number", etmobileNo.text)
                loginUser.put("password", etpassword.text)


                val queue = Volley.newRequestQueue(this)

                val url = "http://13.235.250.119/v2/login/fetch_result"

                val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    loginUser,
                    Response.Listener {

                        val responseJsonObjectData = it.getJSONObject("data")

                        val success = responseJsonObjectData.getBoolean("success")


                        if (success) {

                            val data = responseJsonObjectData.getJSONObject("data")
                            sharedPreferencess.edit().putBoolean("user_logged_in", true).apply()
                            sharedPreferencess.edit().putString("user_id", data.getString("user_id")).apply()
                            sharedPreferencess.edit().putString("name", data.getString("name")).apply()
                            sharedPreferencess.edit().putString("email", data.getString("email")).apply()
                            sharedPreferencess.edit().putString("mobile_number", data.getString("mobile_number")).apply()
                            sharedPreferencess.edit().putString("address", data.getString("address")).apply()

                            Toast.makeText(
                                this,
                                "Welcome "+data.getString("name"),
                                Toast.LENGTH_SHORT
                            ).show()

                            userSuccessfullyLoggedIn()//after we get a response we call the Log the user in

                        } else {

                            btlogin.visibility=View.VISIBLE

                            println(it)

                            val responseMessageServer =
                                responseJsonObjectData.getString("errorMessage")
                            Toast.makeText(
                                this,
                                responseMessageServer.toString(),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    },
                    Response.ErrorListener {
                        println(it)
                        btlogin.visibility=View.VISIBLE

                        Toast.makeText(
                            this,
                            "Some Error occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()



                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()

                        headers["Content-type"] = "application/json"
                        headers["token"] = "12fed3f7359e20"

                        return headers
                    }
                }

                queue.add(jsonObjectRequest)

            } catch (e: JSONException) {
                btlogin.visibility=View.VISIBLE

                Toast.makeText(
                    this,
                    "Some unexpected error occured!!!",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }else
        {
            btlogin.visibility=View.VISIBLE

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("ERROR")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Setting") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                this.finish()
            }
            dialog.setNegativeButton("EXIT") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()

        }

    }

    fun userSuccessfullyLoggedIn(){
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish();
    }



    override fun onResume() {

        if (!ConnectionManager().checkConnectivity(this))
        {

            val alterDialog=androidx.appcompat.app.AlertDialog.Builder(this)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent= Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)
            }

            alterDialog.setNegativeButton("Exit"){ text,listener->
                ActivityCompat.finishAffinity(this)//closes all the instances of the app and the app closes completely
            }
            alterDialog.setCancelable(false)

            alterDialog.create()
            alterDialog.show()

        }

        super.onResume()
    }


}