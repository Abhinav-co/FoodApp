package com.abhi.food_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [RestaurantEntity::class],version = 1)
abstract class RestDatabase :RoomDatabase(){

    abstract fun restDao():RestDao
}