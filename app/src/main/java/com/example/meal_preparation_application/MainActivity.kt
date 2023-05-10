package com.example.meal_preparation_application

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.Meals
import com.example.meal_preparation_application.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    lateinit var searchBar:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mealdatabase").build()
        val mealDao = db.mealDao()

        val addMealsDb = findViewById<Button>(R.id.add_button)
        val SB_igredient=findViewById<Button>(R.id.search_by_ing_button)
        var SB_meal = findViewById<Button>(R.id.search_by_meal_button)
        searchBar = findViewById<EditText>(R.id.search_bar)

        SB_igredient.setOnClickListener{
            val intent = Intent(this, Search_By_Ingredient::class.java)
            startActivity(intent)
        }

        SB_meal.setOnClickListener{
            val intent = Intent(this, Search_for_Meal::class.java)
            startActivity(intent)
        }

        searchBar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(change: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (change.length==1){
                    changePage()
                }
            }
        })

        addMealsDb.setOnClickListener {
            runBlocking {
                launch {

                    mealDao.insert(mealSweetAndSourPork);
                    mealDao.insert(mealChickenMarengo)
                    mealDao.insert(mealBeefBanhMi)
                    mealDao.insert(mealLeblebiSoup)

                    val meals: List<Meals> = mealDao.getAll()

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

    private fun changePage() {
        val intent = Intent(this, Meal_Search::class.java)
        intent.putExtra("search_word",searchBar.text.toString())
        searchBar.text.clear()
        startActivity(intent)
    }
}
