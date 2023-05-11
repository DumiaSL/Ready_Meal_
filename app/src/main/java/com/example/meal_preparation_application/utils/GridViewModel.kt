package com.example.meal_preparation_application.utils

import android.graphics.drawable.BitmapDrawable
import com.example.meal_preparation_application.classes.Meals

//source - https://www.geeksforgeeks.org/android-gridview-in-kotlin/
// Declares a data class named GridViewModal.
// It has two properties: a meal object and a bitmapDrawable object.
data class GridViewModal(
    val meal: Meals, // Represents a meal object from the Meals class.
    var bitmapDrawable: BitmapDrawable? // Represents a bitmapDrawable object that will hold a bitmap image for this meal.
)
