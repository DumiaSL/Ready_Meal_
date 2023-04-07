package com.example.meal_preparation_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.Meals
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

        runBlocking {
            launch {
                val meal = Meals(
                    name = "Leblebi Soup",
                    drinkAlternate = null,
                    category = "Vegetarian",
                    area = "Tunisian",
                    instructions = "Heat the oil in a large pot. Add the onion and cook until translucent.\r\nDrain the soaked chickpeas and add them to the pot together with the vegetable stock. Bring to the boil, then reduce the heat and cover. Simmer for 30 minutes.\r\nIn the meantime toast the cumin in a small ungreased frying pan, then grind them in a mortar. Add the garlic and salt and pound to a fine paste.\r\nAdd the paste and the harissa to the soup and simmer until the chickpeas are tender, about 30 minutes.\r\nSeason to taste with salt, pepper and lemon juice and serve hot.",
                    mealThumb = "https:www.themealdb.comimagesmediamealsx2fw9e1560460636.jpg",
                    ingredients = mutableListOf("Olive Oil", "Onion", "Chickpeas", "Vegetable Stock", "Cumin", "Garlic", "Salt", "Harissa Spice", "Pepper", "Lime"),
//                    measure = mutableListOf("2 tbs", "1 medium finely diced", "250g", "1.5L", "1 tsp", "5 cloves", "12 tsp", "1 tsp", "Pinch", "12"),
//                    ingredients = "Olive Oil",
//                    measure ="1 medium finely diced" ,
                    tags = "Soup",
                    youtube = "https:www.youtube.comwatch?v=BgRifcCwinY",
                    source = "http:allrecipes.co.ukrecipe43419leblebi--tunisian-chickpea-soup-.aspx",
                    imageSource = null,
                    creativeCommonsConfirmed = null,
                    dateModified = null
                )



                mealDao.insert(meal)
                val meals: List<Meals> = mealDao.getAll()
                for (meal_ in meals) {
                    println(meal_)
                }
            }
        }

    }
}