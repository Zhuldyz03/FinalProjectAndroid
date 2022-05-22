package com.example.final_project.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.internshala.foodrunner.database.FavresEntities
import com.internshala.foodrunner.database.favresDao

@Database(entities = [CartEntities::class,FavresEntities::class], version = 1)
abstract class cartDatabase : RoomDatabase() {

     abstract fun favresDao(): favresDao

    abstract fun orderDao(): cartDao

}