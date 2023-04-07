package com.example.meal_preparation_application.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Meals::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun mealDao(): MealDao

}