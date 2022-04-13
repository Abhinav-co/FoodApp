package com.abhi.food_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.abhi.food_app.R
import com.abhi.food_app.model.RestaurantMenues

class RestaurentmenuAdapter(val context: Context, val itemList:ArrayList<RestaurantMenues>):RecyclerView.Adapter<RestaurentmenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclar_menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val restaurants = itemList[position]
        holder.txtMenuName.text = restaurants.menuName
        holder.txtMenuPrice.text = restaurants.menuCostForOne


        holder.buttonAddToCart.setOnClickListener {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()


        }

    }

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMenuName: TextView = view.findViewById(R.id.textViewItemName)
        val txtMenuPrice: TextView = view.findViewById(R.id.textViewItemPrice)
        val buttonAddToCart: Button = view.findViewById(R.id.buttonAddToCart)
    }
}