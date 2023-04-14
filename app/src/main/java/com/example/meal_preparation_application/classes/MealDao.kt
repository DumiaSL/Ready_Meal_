package com.example.meal_preparation_application.classes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
 interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meals)

    @Query("SELECT * FROM meals")
    suspend fun getAll(): List<Meals>

    @Query("DELETE FROM meals")
    suspend fun deleteAll()

   @Query("SELECT id,name FROM meals")
   suspend fun getIdName(): List<Meals>
}