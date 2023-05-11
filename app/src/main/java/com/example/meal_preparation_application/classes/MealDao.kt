package com.example.meal_preparation_application.classes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface MealDao {

    // Inserts a meal object into the database.
    // If there is a conflict, it ignores the new meal and continues without an error.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meals)

    // Retrieves all meals from the database.
    @Query("SELECT * FROM meals")
    suspend fun getAll(): List<Meals>

    // Deletes all meals from the database.
    @Query("DELETE FROM meals")
    suspend fun deleteAll()

    // Retrieves only the id and name of all meals from the database.
    @Query("SELECT id,name FROM meals")
    suspend fun getIdName(): List<Meals>
}
