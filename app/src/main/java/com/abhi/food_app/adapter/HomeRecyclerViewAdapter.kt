package com.abhi.food_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abhi.food_app.R
import com.abhi.food_app.activity.RestaurantMenu
import com.abhi.food_app.model.Restaurants
import com.squareup.picasso.Picasso

class HomeRecyclerViewAdapter(val context: Context,val itemList:ArrayList<Restaurants>):RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recyclar_home_row_item,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurants=itemList[position]
        holder.txtRestName.setTag(restaurants.restaurantId + "")
        holder.txtRestName.text=restaurants.restaurantName
        holder.txtRestRating.text=restaurants.restaurantRating
        holder.txtRestPrice.text=restaurants.restaurantCostForOne + "/person"
        //holder.imgRestImage.setImageResource(restaurants.restaurantImage)
        Picasso.get().load(restaurants.restaurantImage).error(R.drawable.ic_launcher_background).into(holder.imgRestImage)

        holder.llRestContent.setOnClickListener {
            Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
            val intent=Intent(context,RestaurantMenu::class.java)
            intent.putExtra("id",holder.txtRestName.getTag().toString())
            intent.putExtra("name",holder.txtRestName.text.toString())
            context.startActivity(intent)

        }

    }
    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtRestName:TextView=view.findViewById(R.id.txtRestaurentName)
        val txtRestPrice:TextView=view.findViewById(R.id.txtRestPrice)
        val txtRestRating:TextView=view.findViewById(R.id.txtRestRating)
        val imgRestImage:ImageView=view.findViewById(R.id.imgRest)
        val llRestContent:LinearLayout=view.findViewById(R.id.llRestContent)
    }
}