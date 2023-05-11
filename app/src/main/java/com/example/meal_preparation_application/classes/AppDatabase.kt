package com.example.meal_preparation_application.classes

import android.content.Context
import androidx.room.*

// Declares the AppDatabase class as a Room database and specifies the entities it will use.
@Database(entities = [Meals::class], version = 1)
@TypeConverters(StringListTypeConverter:: class)
abstract class AppDatabase : RoomDatabase(){
    // Defines an abstract method that returns an instance of the MealDao interface.
    abstract fun mealDao(): MealDao
}
