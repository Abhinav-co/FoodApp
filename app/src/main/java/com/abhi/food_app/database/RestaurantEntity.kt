package com.abhi.food_app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class RestaurantEntity(
    @PrimaryKey val rest_id: Int,
    val restName: String,
    val rating: String,
    val cost_for_one: String,
    val image: String
)