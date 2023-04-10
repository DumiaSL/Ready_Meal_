package com.example.meal_preparation_application

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.MealDao
import com.example.meal_preparation_application.classes.Meals
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Search_By_Ingredient : AppCompatActivity() {
    var allMeals  = arrayListOf<Meals>();
    lateinit var cardScroll: LinearLayout

    lateinit var linearLayout : LinearLayout
    lateinit var mealDao : MealDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_ingredient)

        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mealdatabase").build()
        mealDao = db.mealDao()


        val searchButton = findViewById<Button>(R.id.search_button)
        val searchTextField = findViewById<EditText>(R.id.search_bar)
        cardScroll = findViewById<LinearLayout>(R.id.scroll_layout)
        val addDbMeals = findViewById<Button>(R.id.saveAllMeal_button)

        //
        addDbMeals.isEnabled=false

        addDbMeals.setOnClickListener {
            for (index in 0 until allMeals.size) {
                runBlocking {
                    launch {
                        mealDao.insert(allMeals[index]);
                    }
                }
            }
            val snackbar = Snackbar.make(addDbMeals, "Successfully added all Meals to DB", Snackbar.LENGTH_LONG).setAction("Action", null)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(Color.parseColor("#FFD200"))
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.BLACK)
            textView.setTypeface(null, Typeface.BOLD)
            textView.textSize = 16f
            snackbar.show()
        }

        searchButton.setOnClickListener {
            //reset meal list
            allMeals  = arrayListOf<Meals>();

            //keyboard hide
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchTextField.windowToken, 0)


            // collecting all the JSON string
            var stb = StringBuilder()
            val url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s="+searchTextField.text.toString()
            val url = URL(url_string)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            runBlocking {
                launch {
                    // run the code of the coroutine in a new thread
                    withContext(Dispatchers.IO) {
                        val bf = BufferedReader(InputStreamReader(con.inputStream))
                        val line: String? = bf.readLine()
                         stb.append(line)
                        if (::linearLayout.isInitialized) {
                            println("reached")
                            runOnUiThread {
                                cardScroll.removeAllViews()
                                addDbMeals.isEnabled=false
                            }
                        }
                        if (parseJSON(stb)){
                            //refresh screen
                            runOnUiThread {
                                addDbMeals.isEnabled=true
                                createMealCards()
                            }
                        }else{
                            val snackbar = Snackbar.make(searchButton, "No Meals Found !!", Snackbar.LENGTH_LONG).setAction("Action", null)
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

    }

    private fun createMealCards() {
        for (index in 0 until allMeals.size) {
            linearLayout = LinearLayout(this) // create a new LinearLayout
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ) // set layout params
            linearLayout.setPadding(20, 20, 20, 0)
            linearLayout.gravity = Gravity.CENTER_HORIZONTAL

            linearLayout.orientation = LinearLayout.VERTICAL // set orientation

            val cardView = CardView(this) // create a new CardView
            cardView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
            ) // set layout params

            cardView.cardElevation = 35f // set cardView properties
            cardView.maxCardElevation = 40f
            cardView.useCompatPadding = true
            cardView.preventCornerOverlap = true
            cardView.radius = 40f


            val innerLinearLayout = LinearLayout(this) // create an inner LinearLayout
            innerLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ) // set layout params
            innerLinearLayout.orientation = LinearLayout.VERTICAL // set orientation

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    460,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                    setMargins(10, 20, 10, 30)
                }
                contentDescription = context.getString(R.string.app_name)
//                setImageResource(R.drawable.foodtest)
            }
            Glide.with(this)
                .load(allMeals[index].mealThumb)
                .into(imageView)

            val mealName = TextView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(260, 20, 0, 20)
                typeface = ResourcesCompat.getFont(context, R.font.poppins_bold)
                text = allMeals[index].name
                setTextColor(Color.parseColor("#0E0E0E"))
                textSize = 26f // 26sp in pixels
                setTypeface(typeface, Typeface.BOLD)
            }

            val mealCategory = TextView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(260, 20, 0, 20)
                typeface = ResourcesCompat.getFont(context, R.font.poppins_bold)
                text = "Category : " + allMeals[index].category
                setTextColor(Color.parseColor("#0E0E0E"))
                textSize = 16f // 26sp in pixels
                setTypeface(typeface, Typeface.BOLD)
            }

            val mealArea = TextView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(260, 20, 0, 40)
                typeface = ResourcesCompat.getFont(context, R.font.poppins_bold)
                text = "Area         : " + allMeals[index].area
                setTextColor(Color.parseColor("#0E0E0E"))
                textSize = 16f // 26sp in pixels
                setTypeface(typeface, Typeface.BOLD)
            }

            val inner1LinearLayout = LinearLayout(this) // create an inner LinearLayout
            innerLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ) // set layout params
            innerLinearLayout.orientation = LinearLayout.VERTICAL // set orientation

            val button = Button(this)
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFD200"))
            button.typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
            button.text = "Save meal to Database"
            button.setTextColor(Color.parseColor("#0E0E0E"))
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            button.setTypeface(button.typeface, Typeface.BOLD)
            button.setOnClickListener {
                runBlocking {
                    launch {
                        mealDao.insert(allMeals[index]);
                    }
                }
                button.isEnabled = false
                button.setText("Already Added to DataBase")
                button.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                button.setTextColor(Color.WHITE)
                val snackbar = Snackbar.make(button, "Successfully added Meal"+allMeals[index].name, Snackbar.LENGTH_LONG).setAction("Action", null)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.parseColor("#FFD200"))
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLACK)
                textView.setTypeface(null, Typeface.BOLD)
                textView.textSize = 16f
                snackbar.show()
            }
            innerLinearLayout.setOnClickListener {
                println("click"+allMeals[index].name)
            }

            innerLinearLayout.addView(imageView) // add views to inner LinearLayout
            innerLinearLayout.addView(mealName)
            innerLinearLayout.addView(mealCategory)
            innerLinearLayout.addView(mealArea)
            inner1LinearLayout.addView(button)
            innerLinearLayout.addView(inner1LinearLayout)

            cardView.addView(innerLinearLayout) // add inner LinearLayout to cardView
            linearLayout.addView(cardView) // add cardView to outer LinearLayout
            cardScroll.addView(linearLayout) // add outer LinearLayout to your layout
        }

    }

    private fun getList(book: JSONObject, typeName: String): ArrayList<String> {
        val temp = ArrayList<String>()
        for (i in 1..20) {
            val type = book[typeName+i.toString()]as? String ?: null
            temp.add(type.toString())
        }
        return temp;
    }

    private fun parseJSON(stb: java.lang.StringBuilder): Boolean {
        // this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
        // Information about all the Meals extracted by this function
        if (json.isNull("meals")){
            return false
        }else {
            val jsonArray: JSONArray = json.getJSONArray("meals")
            // extract all the books from the JSON array
            for (i in 0 until jsonArray.length()) {
                val book: JSONObject = jsonArray[i] as JSONObject // this is a json object

                val meal = Meals(
                    name = book["strMeal"] as? String ?: null,
                    drinkAlternate = book["strDrinkAlternate"] as? String ?: null,
                    category = book["strCategory"] as? String ?: null,
                    area = book["strArea"]as? String ?: null,
                    instructions = book["strInstructions"] as? String ?: null,
                    mealThumb = book["strMealThumb"] as? String ?: null,
                    ingredients = getList(book,"strIngredient"),
                    measure = getList(book,"strMeasure"),
                    tags = book["strTags"] as? String ?: null,
                    youtube = book["strYoutube"] as? String ?: null,
                    source = book["strSource"] as? String ?: null,
                    imageSource = book["strImageSource"] as? String ?: null,
                    creativeCommonsConfirmed = book["strCreativeCommonsConfirmed"] as? String ?: null,
                    dateModified = book["dateModified"] as? String ?: null,
                )
                allMeals.add(meal)
            }
        }
        return true
    }
}