package com.example.meal_preparation_application

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.text.LineBreaker
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.MealDao
import com.example.meal_preparation_application.classes.Meals
import com.example.meal_preparation_application.utils.GridAdeptor
import com.example.meal_preparation_application.utils.GridViewMealModal
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

class Meal_Search : AppCompatActivity() {
    lateinit var searchBar: EditText
    lateinit var SearchButton: Button
    var allMeals = arrayListOf<Meals>()
    var mydialog: Dialog? = null
    lateinit var courseList: List<GridViewMealModal>
    lateinit var courseGRV: GridView
    lateinit var resultCount: TextView
    var bitmapDrawable: BitmapDrawable? = null
    lateinit var mealDao: MealDao
    lateinit var selected_card_list: ArrayList<Meals>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_search)

        mydialog = Dialog(this)

        // create the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mealdatabase").build()
        mealDao = db.mealDao()

        // Retrieve data from previous screen
        var search_word = intent.getStringExtra("search_word").toString()

        // Retrieve a view from the current layout by searching for its ID
        searchBar = findViewById(R.id.search_bar)
        var backIcon = findViewById<TextView>(R.id.backButton)
        SearchButton = findViewById(R.id.search_button)
        resultCount = findViewById(R.id.count_result)
        courseGRV = findViewById(R.id.grid_view_layout)

        // list of what meals add to save
        selected_card_list = ArrayList()

        // if there is data in saved Instance State , getting data
        if (savedInstanceState != null) {
            allMeals = savedInstanceState.getSerializable("allMeals") as ArrayList<Meals>
        }

        // initialize screen
        searchBar.setText(search_word)
        resultCount.isVisible = false

        //Back button
        backIcon.setOnClickListener {
            this.finish()
        }

        //when press search keyboard enter button
        searchBar.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //
                search_fun()
                return@OnKeyListener true
            }
            false
        })

        // click event for search meal button
        SearchButton.setOnClickListener {
            allMeals.clear()
            search_fun()
        }

    }

    //Search Process
    private fun search_fun() {
        //keyboard hide
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchBar.windowToken, 0)


        if (searchBar.text.isNotEmpty()) {
            // clear the saved meal in list
            selected_card_list.clear()

            try {
                // collecting all the JSON string
                val stb = StringBuilder()
                // combind the strings and create link to send http request
                val url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + searchBar.text.toString()
                val url = URL(url_string)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                runBlocking {
                    launch {
                        // run the code of the coroutine in a new thread
                        withContext(Dispatchers.IO) {
                            val bf = BufferedReader(InputStreamReader(con.inputStream))
                            val line: String? = bf.readLine()
                            stb.append(line)

                            //checking is melas have
                            if (parseJSON(stb)) {
                                //
                                runOnUiThread{
                                    createMiniCards()
                                    resultCount.isVisible = true
                                    resultCount.text= "Total Results Found : " + allMeals.size.toString()
                                }

                            } else {
                                val snackbar = Snackbar.make(
                                    SearchButton, "No Meals Found !!", Snackbar.LENGTH_LONG
                                ).setAction("Action", null)
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
            } catch (error: UnknownHostException) {
                //No internet toast message
                val snackbar =
                    Snackbar.make(SearchButton, "No Internet Connection !!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.parseColor("#FFD200"))
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLACK)
                textView.setTypeface(null, Typeface.BOLD)
                textView.textSize = 16f
                snackbar.show()
            } catch (error: FileNotFoundException) {
                //No site reached toast message
                val snackbar = Snackbar.make(
                    SearchButton, "Sorry : Can't reached Server !!", Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.parseColor("#FFD200"))
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLACK)
                textView.setTypeface(null, Typeface.BOLD)
                textView.textSize = 16f
                snackbar.show()
            }
        } else {
            // when the empty text field
            allMeals.clear()
            createMiniCards()
            resultCount.isVisible = false


            //Toast Message
            val snackbar =
                Snackbar.make(SearchButton, "Please Enter Ingredient !!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
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

    private fun createMiniCards() {
        courseList = ArrayList<GridViewMealModal>()
        // on below line we are adding data to
        // our course list with image and course name.
        for (savedMeal in this.allMeals) {
            courseList = courseList + GridViewMealModal(savedMeal, bitmapDrawable)
        }

        // on below line we are initializing our course adapter
        val courseAdapter = GridAdeptor(courseList = courseList, this@Meal_Search)

        // on below line we are setting adapter to our grid view.
        courseGRV.adapter = courseAdapter

        // on below line we are adding on item
        // click listener for our grid view.
        courseGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying

            mydialog!!.setContentView(R.layout.activity_extra_meal_details)
            val extraMealSaveButton = mydialog!!.findViewById<TextView>(R.id.Extrabutton)
            val extraMealName = mydialog!!.findViewById<TextView>(R.id.extra_meal_name)
            val extraMealImage = mydialog!!.findViewById<ImageView>(R.id.extra_meal_image)
            val extraMealCategery = mydialog!!.findViewById<TextView>(R.id.extra_categgory_text)
            val extraMealArea = mydialog!!.findViewById<TextView>(R.id.extra_area_text)
            val extraMealdrink = mydialog!!.findViewById<TextView>(R.id.extra_drink_text)
            val extraMealTags = mydialog!!.findViewById<TextView>(R.id.extra_tag_text)
            val extraMealInstuctions = mydialog!!.findViewById<TextView>(R.id.extra_ins_text)
            val extraMealSource = mydialog!!.findViewById<TextView>(R.id.extra_source)
            val extraMealImgScouce = mydialog!!.findViewById<TextView>(R.id.extra_imagesource)
            val extraMealYoutube = mydialog!!.findViewById<TextView>(R.id.extra_youtube)
            val extraMealCreative = mydialog!!.findViewById<TextView>(R.id.extra_creative)
            val extraMealDate = mydialog!!.findViewById<TextView>(R.id.extra_date)

            val extraMealInglayout = mydialog!!.findViewById<LinearLayout>(R.id.In_layout)
            val extraMealMelayout = mydialog!!.findViewById<LinearLayout>(R.id.meLayout)

            extraMealName.text = courseList[position].meal.name
            extraMealImage.setImageDrawable(courseList[position].bitmapDrawable)
            extraMealCategery.text = "Category : " + courseList[position].meal.category
            extraMealArea.text = "Area : " + courseList[position].meal.area

            if (courseList[position].meal.drinkAlternate != null) {
                extraMealdrink.text = "DrinkAlternate : " + courseList[position].meal.drinkAlternate
            } else {
                extraMealdrink.isVisible = false
            }

            if (courseList[position].meal.source != null) {
                extraMealSource.text = "Source : " + courseList[position].meal.source
                extraMealSource.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(courseList[position].meal.source))
                    startActivity(browserIntent)
                }
            } else {
                extraMealSource.isVisible = false
            }

            if (courseList[position].meal.youtube != null) {
                extraMealYoutube.text = "Youtube : " + courseList[position].meal.youtube
                extraMealYoutube.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(courseList[position].meal.youtube))
                    startActivity(browserIntent)
                }
            } else {
                extraMealYoutube.isVisible = false
            }

            if (courseList[position].meal.imageSource != null) {
                extraMealImgScouce.text = "Image Source : " + courseList[position].meal.imageSource
                extraMealImgScouce.setOnClickListener {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(courseList[position].meal.imageSource)
                    )
                    startActivity(browserIntent)
                }
            } else {
                extraMealImgScouce.isVisible = false
            }

            if (courseList[position].meal.creativeCommonsConfirmed != null) {
                extraMealCreative.text = "Creative Commons Confirmed : " + courseList[position].meal.creativeCommonsConfirmed
            } else {
                extraMealCreative.isVisible = false
            }

            if (courseList[position].meal.dateModified != null) {
                extraMealDate.text = "Date Modified : " + courseList[position].meal.dateModified
            } else {
                extraMealDate.isVisible = false
            }

            if (courseList[position].meal.tags != null) {
                extraMealTags.text = courseList[position].meal.tags
            } else {
                extraMealTags.text = "- -"
            }

            if (courseList[position].meal.instructions != null) {
                extraMealInstuctions.text = courseList[position].meal.instructions
                extraMealInstuctions.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            } else {
                extraMealdrink.text = "- -"
            }

            if (selected_card_list?.contains(courseList[position].meal) == true) {
                extraMealSaveButton.isEnabled = false
                extraMealSaveButton.setText("Already Added to DataBase")
                extraMealSaveButton.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                extraMealSaveButton.setTextColor(Color.WHITE)
            }

            extraMealSaveButton.setOnClickListener {
                selected_card_list.add(courseList[position].meal)
                //
                runBlocking {
                    launch {
                        mealDao.insert(courseList[position].meal);
                    }
                }

                //change the button functionality
                extraMealSaveButton.isEnabled = false
                extraMealSaveButton.setText("Already Added to DataBase")
                extraMealSaveButton.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                extraMealSaveButton.setTextColor(Color.WHITE)

                //Toast message
                val snackbar = Snackbar.make(
                    extraMealSaveButton,
                    "Successfully added Meal " + courseList[position].meal.name,
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.parseColor("#FFD200"))
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLACK)
                textView.setTypeface(null, Typeface.BOLD)
                textView.textSize = 16f
                snackbar.show()
            }

            //removing null values
            val mealIngListWithoutNulls: List<String>? =
                courseList[position].meal.ingredients?.toList()
            val mealMeListWithoutNulls: List<String>? = courseList[position].meal.measure?.toList()

            //removing "", " " , "  " values
            val filteredList_ing = ArrayList<String>()
            val filteredList_me = ArrayList<String>()
            if (mealIngListWithoutNulls != null) {
                for (index in 0 until mealIngListWithoutNulls.size) {
                    if (mealIngListWithoutNulls[index].isNotEmpty() && mealIngListWithoutNulls[index] != "  " && mealIngListWithoutNulls[index] != "   ") {
                        filteredList_ing.add(mealIngListWithoutNulls.get(index))
                    }
                    if (mealMeListWithoutNulls?.get(index)
                            ?.isNotEmpty()!! && mealIngListWithoutNulls[index] != "  " && mealIngListWithoutNulls[index] != "   "
                    ) {
                        filteredList_me.add(mealMeListWithoutNulls.get(index))
                    }
                }
            }

            //
            for (index in 0 until Integer.min(filteredList_ing.size, filteredList_me.size)) {
                val temp_ing = TextView(this)
                temp_ing.id = View.generateViewId()
                temp_ing.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )

                temp_ing.text = filteredList_ing[index]
                temp_ing.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                temp_ing.setTextColor(ContextCompat.getColor(this, R.color.black))
                temp_ing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                temp_ing.typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)

                val temp_me = TextView(this)
                temp_me.id = View.generateViewId()
                temp_me.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                temp_me.text = filteredList_me[index]
                temp_me.textAlignment = View.TEXT_ALIGNMENT_CENTER
                temp_me.setTextColor(ContextCompat.getColor(this, R.color.black))
                temp_me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                temp_me.typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
                // add the textView to a parent layout
                extraMealMelayout.addView(temp_me)
                extraMealInglayout.addView(temp_ing)
            }
            mydialog!!.show()
        }
    }


    private fun parseJSON(stb: java.lang.StringBuilder): Boolean {
        //reset All meal List
        allMeals = arrayListOf<Meals>();

        // this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
        // Information about all the Meals extracted by this function
        if (json.isNull("meals")) {
            return false
        } else {
            val jsonArray: JSONArray = json.getJSONArray("meals")
            // extract all the books from the JSON array
            for (i in 0 until jsonArray.length()) {
                val jsonMealList: JSONObject = jsonArray[i] as JSONObject // this is a json object

                val meal = Meals(
                    name = jsonMealList["strMeal"] as? String ?: null,
                    drinkAlternate = jsonMealList["strDrinkAlternate"] as? String ?: null,
                    category = jsonMealList["strCategory"] as? String ?: null,
                    area = jsonMealList["strArea"] as? String ?: null,
                    instructions = jsonMealList["strInstructions"] as? String ?: null,
                    mealThumb = jsonMealList["strMealThumb"] as? String ?: null,
                    ingredients = getList(jsonMealList, "strIngredient"),
                    measure = getList(jsonMealList, "strMeasure"),
                    tags = jsonMealList["strTags"] as? String ?: null,
                    youtube = jsonMealList["strYoutube"] as? String ?: null,
                    source = jsonMealList["strSource"] as? String ?: null,
                    imageSource = jsonMealList["strImageSource"] as? String ?: null,
                    creativeCommonsConfirmed = jsonMealList["strCreativeCommonsConfirmed"] as? String
                        ?: null,
                    dateModified = jsonMealList["dateModified"] as? String ?: null,
                )
                allMeals.add(meal)
            }
        }
        return true
    }

    // get the list of ingredient and measures
    private fun getList(jsonMealList: JSONObject, typeName: String): ArrayList<String> {
        val temp = ArrayList<String>()
        for (i in 1..20) {
            val type = jsonMealList[typeName + i.toString()] as? String
            temp.add(type.toString())
        }
        return temp;
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("allMeals", allMeals)
        outState.putSerializable("selected_card_list", selected_card_list)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        allMeals = savedInstanceState.getSerializable("allMeals") as ArrayList<Meals>
        selected_card_list = savedInstanceState.getSerializable("selected_card_list") as ArrayList<Meals>

        whenRotateSet()
    }

    // when the screen rotate
    private fun whenRotateSet() {
        if (allMeals.size!=0){
            resultCount.isVisible = true
            resultCount.text= "Total Results Found : " + allMeals.size.toString()
            createMiniCards()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mydialog != null && mydialog!!.isShowing()) {
            mydialog!!.dismiss()
        }
    }
}