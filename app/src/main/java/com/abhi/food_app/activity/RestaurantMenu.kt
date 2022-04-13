package com.abhi.food_app.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.food_app.R
import com.abhi.food_app.adapter.HomeRecyclerViewAdapter
import com.abhi.food_app.adapter.RestaurentmenuAdapter
import com.abhi.food_app.model.RestaurantMenues
import com.abhi.food_app.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.home_dashboard.*
import org.json.JSONException

class RestaurantMenu : AppCompatActivity() {

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter:RestaurentmenuAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var btnAddCart:Button
    lateinit var addToCartLayout:RelativeLayout
    var restId: String? = "100"
    var restName: String?="abc"
    val itemList = arrayListOf<RestaurantMenues>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restraunt_menu)

        progressLayout = findViewById(R.id.progressbarLayout)
        progressLayout.visibility=View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        toolbar = findViewById(R.id.toolbar)
        btnAddCart=findViewById(R.id.btAddToCart)
        addToCartLayout=findViewById((R.id.addToCartLayout))
        addToCartLayout.visibility=View.INVISIBLE
        coordinatorLayout = findViewById(R.id.cordinatorLayout)
        recyclerView = findViewById(R.id.recyclarMenu)

        layoutManager = LinearLayoutManager(this)
        if (intent != null) {
            restId = intent.getStringExtra("id")
        } else {
            Toast.makeText(this, "Some Unexpected Error", Toast.LENGTH_SHORT).show()
        }
        if (restId=="100"){
            Toast.makeText(this, "Some Unexpected Error", Toast.LENGTH_SHORT).show()
        }
        setupToolBar()
        restMenu()

    }

    fun restMenu(){

        val queue = Volley.newRequestQueue(this)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restId"
        if (ConnectionManager().checkConnectivity(this)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try {

                        progressLayout.visibility = View.GONE
                        val responseJsonObjectData = it.getJSONObject("data")
                        val success = responseJsonObjectData.getBoolean("success")
                        if (success) {
                            val data = responseJsonObjectData.getJSONArray("data")
                            for (j in 0 until data.length()) {
                                val restJsonObject = data.getJSONObject(j)
                                val restObject = RestaurantMenues(
                                    restJsonObject.getString("id"),
                                    restJsonObject.getString("name"),
                                    restJsonObject.getString("cost_for_one")
                                )
                                itemList.add(restObject)
                                recyclerAdapter =
                                    RestaurentmenuAdapter(this, itemList)
                                recyclerView.adapter = recyclerAdapter
                                recyclerView.layoutManager = layoutManager

                            }
                        } else {
                            Toast.makeText(this, "ERROR!!", Toast.LENGTH_SHORT)
                                .show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this,
                            "UNEXPECTED this ERROR!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }


                }, Response.ErrorListener {
                    Toast.makeText(
                        this,
                        "UNEXPECTED Volley ERROR!!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "12fed3f7359e20"
                        return headers
                    }
                }
            //12fed3f7359e20

            queue.add(jsonObjectRequest)

        } else {
            //InternetisnotAvailable
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
    private fun setupToolBar() {
        if(restName!=null){
            restName=intent.getStringExtra("name")
        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.title = restName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}













