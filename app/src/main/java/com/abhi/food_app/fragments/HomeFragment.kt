package com.abhi.food_app.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.food_app.R
import com.abhi.food_app.adapter.HomeRecyclerViewAdapter
import com.abhi.food_app.model.Restaurants
import com.abhi.food_app.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class HomeFragment : Fragment() {

    lateinit var recyclarHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    val itemList = arrayListOf<Restaurants>()
    lateinit var recyclarAdapter: HomeRecyclerViewAdapter
    lateinit var progressBar: ProgressBar
    lateinit var progressBarLayout: RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.home_dashboard, container, false)

        setHasOptionsMenu(true)
        recyclarHome = view.findViewById(R.id.recyclarHome)
        progressBar = view.findViewById(R.id.progressBar)
        progressBarLayout = view.findViewById(R.id.progressbarLayout)
        progressBarLayout.visibility = View.VISIBLE


        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try {

                        progressBarLayout.visibility = View.GONE
                        val responseJsonObjectData = it.getJSONObject("data")
                        val success = responseJsonObjectData.getBoolean("success")
                        if (success) {
                            val data = responseJsonObjectData.getJSONArray("data")
                            for (j in 0 until data.length()) {
                                val restJsonObject = data.getJSONObject(j)
                                val restObject = Restaurants(
                                    restJsonObject.getString("id"),
                                    restJsonObject.getString("name"),
                                    restJsonObject.getString("rating"),
                                    restJsonObject.getString("cost_for_one"),
                                    restJsonObject.getString("image_url")
                                )
                                itemList.add(restObject)
                                recyclarAdapter =
                                    HomeRecyclerViewAdapter(activity as Context, itemList)
                                recyclarHome.adapter = recyclarAdapter
                                recyclarHome.layoutManager = layoutManager

                            }
                        } else {
                            Toast.makeText(activity as Context, "ERROR!!", Toast.LENGTH_SHORT)
                                .show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "UNEXPECTED this ERROR!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }


                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "UNEXPECTED Volley ERROR!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("ERROR")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Setting") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("EXIT") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view
    }

}