package com.example.meal_preparation_application

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.text.LineBreaker
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.Meals
import com.example.meal_preparation_application.utils.GridRVAdeptor
import com.example.meal_preparation_application.utils.GridViewModal
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Integer.min

class Search_for_Meal : AppCompatActivity() {
    // on below line we are creating
    // variables for grid view and course list
    lateinit var courseGRV: GridView
    lateinit var searchbarTextField : EditText
    lateinit var resultCount : TextView
    lateinit var courseList: List<GridViewModal>
    var mydialog: Dialog? = null
    lateinit var selectedMeals: MutableList<Meals>
    var bitmapDrawable: BitmapDrawable? = null
    lateinit var allMeals: List<Meals>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_meal)

        selectedMeals = mutableListOf()
        courseGRV = findViewById(R.id.grid_view_layout)
        searchbarTextField = findViewById<EditText>(R.id.search_bar)
        resultCount = findViewById<TextView>(R.id.count_result)

        mydialog = Dialog(this)

        // create the database
        val db = Room.databaseBuilder(
            this, AppDatabase::class.java,
            "mealdatabase"
        ).build()
        val mealDao = db.mealDao()

        //getting all the saved data
        runBlocking {
            launch {
                allMeals = mealDao.getAll()
            }
        }

        resultCount.isVisible = false

        //
        searchbarTextField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                onChangeWord: CharSequence, start: Int,
                before: Int, count: Int
            )
            {
                //reset lists
                val mealsBasedOnName = ArrayList<Meals>()
                val mealsBasedOnIngredient = ArrayList<Meals>()
                courseList = ArrayList<GridViewModal>()
                selectedMeals.clear()

                //
                for (meal in allMeals) {
                    //
                    if (meal.name?.contains(
                            onChangeWord,
                            ignoreCase = true
                        ) == true
                    ) mealsBasedOnName.add(meal)

                    //
                    for (ingredient in meal.ingredients!!) {
                        if (ingredient.contains(onChangeWord, ignoreCase = true)) {
                            mealsBasedOnIngredient.add(meal)
                            break
                        }
                    }
                    selectedMeals =
                        (mealsBasedOnIngredient + mealsBasedOnName).distinct().toMutableList()
                }
                resultCount.isVisible = true
                resultCount.text =
                    "Total Results Found : " + selectedMeals.size.toString()
                //
                createMiniCards()
            }
        })
    }

    private fun createMiniCards() {
        //
        if (searchbarTextField.text.isEmpty()){
            resultCount.isVisible = false
            this.selectedMeals.clear()
        }
        // on below line we are adding data to
        // our course list with image and course name.
        for (savedMeal in this.selectedMeals) {
            courseList = courseList + GridViewModal(savedMeal,bitmapDrawable)
        }


        // on below line we are initializing our course adapter
        // and passing course list and context.
        val courseAdapter = GridRVAdeptor(courseList = courseList, this@Search_for_Meal)

        // on below line we are setting adapter to our grid view.
        courseGRV.adapter = courseAdapter

        // on below line we are adding on item
        // click listener for our grid view.
        courseGRV.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                // inside on click method we are simply displaying

                mydialog!!.setContentView(R.layout.activity_extra_meal_details)
                val extraMealSaveButton = mydialog!!.findViewById<TextView>(R.id.Extrabutton)
                extraMealSaveButton.isVisible = false
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
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(courseList[position].meal.imageSource))
                        startActivity(browserIntent)
                    }
                } else {
                    extraMealImgScouce.isVisible = false
                }

                if (courseList[position].meal.creativeCommonsConfirmed != null) {
                    extraMealCreative.text =
                        "Creative Commons Confirmed : " + courseList[position].meal.creativeCommonsConfirmed
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
                    extraMealInstuctions.justificationMode =
                        LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                } else {
                    extraMealdrink.text = "- -"
                }


                //removing null values
                val mealIngListWithoutNulls: List<String>? = courseList[position].meal.ingredients?.toList()
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
                for (index in 0 until min(filteredList_ing.size, filteredList_me.size)) {
                    val temp_ing = TextView(this)
                    temp_ing.id = View.generateViewId()
                    temp_ing.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    temp_ing.text = filteredList_ing[index]
                    temp_ing.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    temp_ing.setTextColor(ContextCompat.getColor(this, R.color.black))
                    temp_ing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                    temp_ing.typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)

                    val temp_me = TextView(this)
                    temp_me.id = View.generateViewId()
                    temp_me.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
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
}