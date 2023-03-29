package com.example.meal_preparation_application.classes

import androidx.room.Insert
import androidx.room.Query

public interface MealDao {
    @Insert
    suspend fun insert(meal: Meal)

    @Query("SELECT * FROM meals")
    suspend fun getAll(): List<Meal>

    @Query("DELETE FROM meals")
    suspend fun deleteAll()
}