package com.example.meal_preparation_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.meal_preparation_application.utils.GridRVAdeptor
import com.example.meal_preparation_application.utils.GridViewModal

class Search_for_Meal : AppCompatActivity() {
    // on below line we are creating
    // variables for grid view and course list
    lateinit var courseGRV: GridView
    lateinit var courseList: List<GridViewModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_meal)

        // initializing variables of grid view with their ids.
        courseGRV = findViewById(R.id.grid_view_layout)
        courseList = ArrayList<GridViewModal>()

        // on below line we are adding data to
        // our course list with image and course name.
        courseList = courseList + GridViewModal("C++", R.drawable.foodtest)
        courseList = courseList + GridViewModal("Java", R.drawable.foodtest)
        courseList = courseList + GridViewModal("Android", R.drawable.foodtest)
        courseList = courseList + GridViewModal("Python", R.drawable.foodtest)
        courseList = courseList + GridViewModal("Javascript", R.drawable.foodtest)

        // on below line we are initializing our course adapter
        // and passing course list and context.
        val courseAdapter = GridRVAdeptor(courseList = courseList, this)

        // on below line we are setting adapter to our grid view.
        courseGRV.adapter = courseAdapter

        // on below line we are adding on item
        // click listener for our grid view.
        courseGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying
            // a toast message with course name.
            Toast.makeText(
                applicationContext, courseList[position].courseName + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}