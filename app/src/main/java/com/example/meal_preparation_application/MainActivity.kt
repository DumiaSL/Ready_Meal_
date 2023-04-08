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

        var addMealsDb = findViewById<Button>(R.id.add_button)
        var SB_ngredient=findViewById<Button>(R.id.search_by_ing_button)

        SB_ngredient.setOnClickListener{
            val intent = Intent(this, Search_By_Ingredient::class.java)
            startActivity(intent)
        }

        addMealsDb.setOnClickListener {
            runBlocking {
                launch {

                    val mealSweetAndSourPork = Meals(
                        name = "Sweet and Sour Pork",
                        category = "Pork",
                        area = "Chinese",
                        instructions = "Preparation\r1. Crack the egg into a bowl. Separate the egg white and yolk.\r\nSweet and Sour Pork\r2. Slice the pork tenderloin into ips.\r\n3. Prepare the marinade using a pinch of salt, one teaspoon of starch, two teaspoons of light soy sauce, and an egg white.\r\n4. Marinade the pork ips for about 20 minutes.\r\n5. Put the remaining starch in a bowl. Add some water and vinegar to make a starchy sauce.\r\nSweet and Sour Pork\rCooking Instructions\r1. Pour the cooking oil into a wok and heat to 190\u00b0C (375\u00b0F). Add the marinated pork ips and fry them until they turn brown. Remove the cooked pork from the wok and place on a plate.\r\n2. Leave some oil in the wok. Put the tomato sauce and white sugar into the wok, and heat until the oil and sauce are fully combined.\r\n3. Add some water to the wok and thoroughly heat the sweet and sour sauce before adding the pork ips to it.\r\n4. Pour in the starchy sauce. Stir-fry all the ingredients until the pork and sauce are thoroughly mixed together.\r\n5. Serve on a plate and add some coriander for decoration.",
                        mealThumb = "https://www.themealdb.com/images/media/meals/1529442316.jpg",
                        tags = "Sweet",
                        youtube =  "https:/www.youtube.com/watch?v=mdaBIhgEAMo",
                        ingredients= arrayListOf<String>("Pork","Egg","Water","Salt","Sugar","Soy Sauce","Starch","Tomato Puree","Vinegar","Coriander"),
                        measure = arrayListOf<String>("200g","1","Dash","1//2 tsp","1 tsp","10g","10g","30g","10g","Dash")
                    )

                    val mealChickenMarengo = Meals(
                        name = "Chicken Marengo",
                        category = "Chicken",
                        area = "French",
                        instructions = "Heat the oil in a large flameproof casserole dish and stir-fry the mushrooms until they start to soften. Add the chicken legs and cook briefly on each side to colour them a little.\\r\\nPour in the passata, crumble in the stock cube and stir in the olives. Season with black pepper \\u2013 you should\\u2019t need salt. Cover and simmer for 40 mins until the chicken is tender. Sprinkle with parsley and serve with pasta and a salad, or mash and green veg, if you like.",
                        mealThumb = "https://www.themealdb.com/images/media/meals/qpxvuq1511798906.jpg",
                        youtube = "https://www.youtube.com/watch?v=U33HYUr-0Fw",
                        ingredients= arrayListOf<String>("Olive Oil","Mushrooms","Chicken Legs","Passata","Chicken Stock Cube","Black Olives","Parsley"),
                        measure = arrayListOf<String>("1 tbs","300g","4","500g","1","100g","Chopped"),
                        source = "https://www.bbcgoodfood.com/recipes/3146682/chicken-marengo",
                    )

                    val mealBeefBanhMi = Meals(
                        name = "Beef Banh Mi Bowls with Sriracha Mayo, Carrot & Pickled Cucumber",
                        category = "Beef",
                        area = "Vietnamese",
                        instructions = "Add'l ingredients: mayonnaise, siracha\r\n\r\n1\r\n\r\nPlace rice in a fine-mesh sieve and rinse until water runs clear. Add to a small pot with 1 cup water (2 cups for 4 servings) and a pinch of salt. Bring to a boil, then cover and reduce heat to low. Cook until rice is tender, 15 minutes. Keep covered off heat for at least 10 minutes or until ready to serve.\r\n\r\n2\r\n\r\nMeanwhile, wash and dry all produce. Peel and finely chop garlic. Zest and quarter lime (for 4 servings, zest 1 lime and quarter both). Trim and halve cucumber lengthwise; thinly slice crosswise into half-moons. Halve, peel, and medium dice onion. Trim, peel, and grate carrot.\r\n\r\n3\r\n\r\nIn a medium bowl, combine cucumber, juice from half the lime, \u00bc tsp sugar (\u00bd tsp for 4 servings), and a pinch of salt. In a small bowl, combine mayonnaise, a pinch of garlic, a squeeze of lime juice, and as much sriracha as you\u2019d like. Season with salt and pepper.\r\n\r\n4\r\n\r\nHeat a drizzle of oil in a large pan over medium-high heat. Add onion and cook, stirring, until softened, 4-5 minutes. Add beef, remaining garlic, and 2 tsp sugar (4 tsp for 4 servings). Cook, breaking up meat into pieces, until beef is browned and cooked through, 4-5 minutes. Stir in soy sauce. Turn off heat; taste and season with salt and pepper.\r\n\r\n5\r\n\r\nFluff rice with a fork; stir in lime zest and 1 TBSP butter. Divide rice between bowls. Arrange beef, grated carrot, and pickled cucumber on top. Top with a squeeze of lime juice. Drizzle with sriracha mayo.",
                        mealThumb = "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg",
                        ingredients = arrayListOf<String>("Rice","Onion","Lime","Garlic Clove","Cucumber","Carrots","Ground Beef","Soy Sauce"),
                        measure = arrayListOf<String>("White","1","1","3","1","3 oz ","1 lb","2 oz ",)
                    )

                    val mealLeblebiSoup = Meals(
                        name = "Leblebi Soup",
                        category = "Vegetarian",
                        area = "Tunisian",
                        instructions = "Heat the oil in a large pot. Add the onion and cook until translucent.\r\nDrain the soaked chickpeas and add them to the pot together with the vegetable stock. Bring to the boil, then reduce the heat and cover. Simmer for 30 minutes.\r\nIn the meantime toast the cumin in a small ungreased frying pan, then grind them in a mortar. Add the garlic and salt and pound to a fine paste.\r\nAdd the paste and the harissa to the soup and simmer until the chickpeas are tender, about 30 minutes.\r\nSeason to taste with salt, pepper and lemon juice and serve hot.",
                        mealThumb = "https://www.themealdb.com/images/media/meals/x2fw9e1560460636.jpg",
                        tags = "Soup",
                        youtube = "https://www.youtube.com/watch?v=BgRifcCwinY",
                        ingredients = arrayListOf<String>("Olive Oil","Onion","Chickpeas","Vegetable Stock","Cumin","Garlic","Salt","Harissa Spice","Pepper","Lime"),
                        measure = arrayListOf<String>("2 tbs","1 medium finely diced","250g","1.5L","1 tsp ","5 cloves","1/2 tsp","1 tsp ","","1/2 "),
                        source = "http://allrecipes.co.uk/recipe/43419/leblebi--tunisian-chickpea-soup-.aspx",
                    )
                    mealDao.insert(mealSweetAndSourPork);
                    mealDao.insert(mealChickenMarengo)
                    mealDao.insert(mealBeefBanhMi)
                    mealDao.insert(mealLeblebiSoup)
                    val meals: List<Meals> = mealDao.getAll()
                    for (meal_ in meals) {
                        println(meal_)
                    }
//                    Toast.makeText(this@MainActivity, "Successfully added!", Toast.LENGTH_SHORT).show()
                    //Snackbar(view)
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
