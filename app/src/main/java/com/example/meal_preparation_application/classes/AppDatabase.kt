package com.example.meal_preparation_application.classes

import android.content.Context
import androidx.room.*

@Database(entities = [Meals::class], version = 1)
@TypeConverters(StringListTypeConverter:: class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun mealDao(): MealDao
}