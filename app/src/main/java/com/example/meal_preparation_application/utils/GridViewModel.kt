package com.example.meal_preparation_application.utils

// on below line we are creating a modal class.
data class GridViewModal(
    // we are creating a modal class with 2 member
    // one is course name as string and
    // other course img as int.
    val meal_name: String,
    val meal_thumbnail: String,
    val meal_category: String
)