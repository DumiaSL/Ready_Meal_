package com.example.meal_preparation_application

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.Meals
import com.example.meal_preparation_application.utils.GridRVAdeptor
import com.example.meal_preparation_application.utils.GridViewModal
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Search_for_Meal : AppCompatActivity() {
    // on below line we are creating
    // variables for grid view and course list
    lateinit var courseGRV: GridView
    lateinit var courseList: List<GridViewModal>

    lateinit var  meals: List<Meals>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_meal)

        // create the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mealdatabase").build()
        val mealDao = db.mealDao()

        // initializing variables of grid view with their ids.
        courseGRV = findViewById(R.id.grid_view_layout)
        courseList = ArrayList<GridViewModal>()


        //
        runBlocking {
            launch{
                meals= mealDao.getAll()
            }
        }

        for (savedMeal in meals) {
            courseList = courseList + GridViewModal(savedMeal.name as String,savedMeal.mealThumb as String,savedMeal.category as String)
        }


        // on below line we are adding data to
        // our course list with image and course name.

//        courseList = courseList + GridViewModal("Java", R.drawable.foodtest, "Fish")
//        courseList = courseList + GridViewModal("Android", R.drawable.foodtest, "Fish")
//        courseList = courseList + GridViewModal("Python", R.drawable.foodtest, "Fish")
//        courseList = courseList + GridViewModal("Javascript", R.drawable.foodtest, "Fish")

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
                applicationContext, courseList[position].meal_name + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun loadImage(imageLink: String): BitmapDrawable? {
        var bitmapDrawable: BitmapDrawable? = null
        println(imageLink)
        Glide.with(this)
            .asBitmap()
            .load(imageLink)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    bitmapDrawable = BitmapDrawable(resources, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })
        return bitmapDrawable
    }
}