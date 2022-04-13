package com.abhi.food_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestDao {
    @Insert
    fun insertRest(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRest(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurant")
    fun getAllrest(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurant WHERE rest_id= :restId")
    fun getRestById(restId: String):RestaurantEntity
}