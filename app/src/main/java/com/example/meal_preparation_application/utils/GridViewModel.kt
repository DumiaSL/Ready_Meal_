package com.example.meal_preparation_application.utils

import android.graphics.drawable.BitmapDrawable
import com.example.meal_preparation_application.classes.Meals

//source - https://www.geeksforgeeks.org/android-gridview-in-kotlin/
// on below line we are creating a modal class.
data class GridViewModal(
    // we are creating a modal class with 2 member
    // one is course name as string and
    // other course img as int.
    val meal: Meals,
    var bitmapDrawable: BitmapDrawable?
)