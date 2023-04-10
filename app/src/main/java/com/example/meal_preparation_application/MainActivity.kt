package com.example.meal_preparation_application

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.Meals
import com.example.meal_preparation_application.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mealdatabase").build()
        val mealDao = db.mealDao()

        val addMealsDb = findViewById<Button>(R.id.add_button)
        val SB_ngredient=findViewById<Button>(R.id.search_by_ing_button)

        SB_ngredient.setOnClickListener{
            val intent = Intent(this, Search_By_Ingredient::class.java)
            startActivity(intent)
        }

        addMealsDb.setOnClickListener {
            runBlocking {
                launch {
                    //
                    mealDao.insert(mealSweetAndSourPork);
                    mealDao.insert(mealChickenMarengo)
                    mealDao.insert(mealBeefBanhMi)
                    mealDao.insert(mealLeblebiSoup)

                    val meals: List<Meals> = mealDao.getAll()
                    for (meal_ in meals) {
                        println(meal_)
                    }

                    val snackbar = Snackbar.make(addMealsDb, "Successfully added",
                        Snackbar.LENGTH_LONG).setAction("Action", null)
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.parseColor("#FFD200"))
                    val textView =
                        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.BLACK)
                    textView.setTypeface(null, Typeface.BOLD)
                    textView.textSize = 16f
                    snackbar.show()
                }
            }
        }

    }
}
