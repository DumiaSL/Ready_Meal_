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
        var DrinkAlternate: String,
        var Category: String,
        var Instructions: String,
        var MealThumb: String,
        var ingredients: MutableList<String> = mutableListOf<String>(),
        val imageUrl: String
    )

}