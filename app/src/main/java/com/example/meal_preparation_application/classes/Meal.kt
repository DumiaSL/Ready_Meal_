package com.example.meal_preparation_application.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
public
class Meal {
    data class Meal(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val description: String,
        val calories: Int,
        val imageUrl: String
    )

}