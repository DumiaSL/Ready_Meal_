package com.example.meal_preparation_application

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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

    var mydialog: Dialog? = null
    lateinit var Controllist:ArrayList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_ingredient)

        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mealdatabase").build()
        mealDao = db.mealDao()

        mydialog=Dialog(this)
        Controllist = ArrayList()

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchTextField = findViewById<EditText>(R.id.search_bar)
        cardScroll = findViewById<LinearLayout>(R.id.scroll_layout)
        val addDbMeals = findViewById<Button>(R.id.saveAllMeal_button)

        //
        addDbMeals.isEnabled=false

        addDbMeals.setOnClickListener {
            println(Controllist)
            for (index in 0 until allMeals.size) {
                runBlocking {
                    launch {
                        mealDao.insert(allMeals[index]);
                        Controllist[index].isEnabled = false
                        Controllist[index].setText("Already Added to DataBase")
                        Controllist[index].backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                        Controllist[index].setTextColor(Color.WHITE)
                    }
                }
            }
            addDbMeals.isEnabled=false
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
            //keyboard hide
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchTextField.windowToken, 0)

            if (searchTextField.text.isNotEmpty()){
                //reset meal list
                allMeals  = arrayListOf<Meals>();
                addDbMeals.isEnabled=true

                // collecting all the JSON string
                val stb = StringBuilder()
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
                                val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                                textView.setTextColor(Color.BLACK)
                                textView.setTypeface(null, Typeface.BOLD)
                                textView.textSize = 16f
                                snackbar.show()
                            }
                        }
                    }
                }
            }else{
                if (::linearLayout.isInitialized) {
                    runOnUiThread {
                        cardScroll.removeAllViews()
                        addDbMeals.isEnabled=false
                    }
                }
                val snackbar = Snackbar.make(searchButton, "Not field !!", Snackbar.LENGTH_LONG).setAction("Action", null)
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

    private fun createMealCards() {
        Controllist.clear()
        for (index in 0 until allMeals.size) {
            var isSelect = false
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
                    setMargins(10, 20, 10, 0)

                }
                contentDescription = context.getString(R.string.app_name)
            }

            var bitmapDrawable: BitmapDrawable? =null
            Glide.with(this)
                .asBitmap()
                .load(allMeals[index].mealThumb)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        imageView.setImageBitmap(resource)

                        bitmapDrawable = BitmapDrawable(resources, resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })

            val mealName = TextView(this)
            mealName.text = allMeals[index].name
            mealName.gravity = Gravity.CENTER_HORIZONTAL
            mealName.typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
            mealName.setTextColor(ContextCompat.getColor(this, R.color.black))
            mealName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35f)

            val mealCategory = TextView(this)
            mealCategory.text = "Category : " + allMeals[index].category
            mealCategory.gravity = Gravity.CENTER_HORIZONTAL
            mealCategory.typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
            mealCategory.setTextColor(ContextCompat.getColor(this, R.color.black))
            mealCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

            val mealArea = TextView(this)
            mealArea.text = "Area         : " + allMeals[index].area
            mealArea.gravity = Gravity.CENTER_HORIZONTAL
            mealArea.typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
            mealArea.setTextColor(ContextCompat.getColor(this, R.color.black))
            mealArea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

            val inner1LinearLayout = LinearLayout(this) // create an inner LinearLayout
            innerLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ) .apply {
                setMargins(0, 10,0, 0)
            }// set layout params
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
                isSelect = true
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
            Controllist.add(button)

            //
            innerLinearLayout.setOnClickListener {
                mydialog!!.setContentView(R.layout.activity_extra_meal_details)
                val extraMealSaveButton = mydialog!!.findViewById<TextView>(R.id.Extrabutton)
                val extraMealName = mydialog!!.findViewById<TextView>(R.id.extra_meal_name)
                val extraMealImage = mydialog!!.findViewById<ImageView>(R.id.extra_meal_image)
                val extraMealCategery = mydialog!!.findViewById<TextView>(R.id.extra_categgory_text)
                val extraMealArea= mydialog!!.findViewById<TextView>(R.id.extra_area_text)
                val extraMealdrink= mydialog!!.findViewById<TextView>(R.id.extra_drink_text)
                val extraMealTags= mydialog!!.findViewById<TextView>(R.id.extra_tag_text)
                val extraMealInstuctions= mydialog!!.findViewById<TextView>(R.id.extra_ins_text)
                val extraMealSource = mydialog!!.findViewById<TextView>(R.id.extra_source)
                val extraMealImgScouce = mydialog!!.findViewById<TextView>(R.id.extra_imagesource)
                val extraMealYoutube = mydialog!!.findViewById<TextView>(R.id.extra_youtube)
                val extraMealCreative = mydialog!!.findViewById<TextView>(R.id.extra_creative)
                val extraMealDate = mydialog!!.findViewById<TextView>(R.id.extra_date)

                val extraMealInglayout = mydialog!!.findViewById<LinearLayout>(R.id.In_layout)
                val extraMealMelayout = mydialog!!.findViewById<LinearLayout>(R.id.meLayout)


                if (allMeals[index].drinkAlternate!=null){
                    extraMealdrink.text = "DrinkAlternate : " + allMeals[index].drinkAlternate
                }else{
                    extraMealdrink.isVisible = false
                }

                if (allMeals[index].source!=null){
                    extraMealSource.text = "Source : "+allMeals[index].source
                    extraMealSource.setOnClickListener {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(allMeals[index].source))
                        startActivity(browserIntent)
                    }
                }else{
                    extraMealSource.isVisible = false
                }

                if (allMeals[index].youtube!=null){
                    extraMealYoutube.text = "Youtube : "+  allMeals[index].youtube
                    extraMealYoutube.setOnClickListener {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(allMeals[index].youtube))
                        startActivity(browserIntent)
                    }
                }else{
                    extraMealYoutube.isVisible = false
                }

                if (allMeals[index].imageSource!=null){
                    extraMealImgScouce.text = "Image Source : "+ allMeals[index].imageSource
                    extraMealImgScouce.setOnClickListener {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(allMeals[index].imageSource))
                        startActivity(browserIntent)
                    }
                }else{
                    extraMealImgScouce.isVisible = false
                }

                if (allMeals[index].creativeCommonsConfirmed!=null){
                    extraMealCreative.text = "Creative Commons Confirmed : " + allMeals[index].creativeCommonsConfirmed
                }else{
                    extraMealCreative.isVisible = false
                }

                if (allMeals[index].dateModified!=null){
                    extraMealDate.text = "Date Modified : " + allMeals[index].dateModified
                }else{
                    extraMealDate.isVisible = false
                }

                if (allMeals[index].tags!=null){
                    extraMealTags.text = allMeals[index].tags
                }else{
                    extraMealTags.text = "- -"
                }

                if (allMeals[index].instructions != null){
                    extraMealInstuctions.text = allMeals[index].instructions
                    extraMealInstuctions.justificationMode = JUSTIFICATION_MODE_INTER_WORD
                }else{
                    extraMealdrink.text = "- -"
                }

                if (isSelect){
                    extraMealSaveButton.isEnabled = false
                    extraMealSaveButton.setText("Already Added to DataBase")
                    extraMealSaveButton.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                    extraMealSaveButton.setTextColor(Color.WHITE)
                }

                extraMealName.text = allMeals[index].name
                extraMealImage.setImageDrawable(bitmapDrawable)
                extraMealCategery.text = "Category : "+ allMeals[index].category
                extraMealArea.text = "Area : "+ allMeals[index].area

                extraMealSaveButton.setOnClickListener {
                    runBlocking {
                        launch {
                            mealDao.insert(allMeals[index]);
                        }
                    }
                    button.isEnabled = false
                    button.setText("Already Added to DataBase")
                    button.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                    button.setTextColor(Color.WHITE)
                    extraMealSaveButton.isEnabled = false
                    extraMealSaveButton.setText("Already Added to DataBase")
                    extraMealSaveButton.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                    extraMealSaveButton.setTextColor(Color.WHITE)
                    val snackbar = Snackbar.make(extraMealSaveButton, "Successfully added Meal"+allMeals[index].name, Snackbar.LENGTH_LONG).setAction("Action", null)
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.parseColor("#FFD200"))
                    val textView =
                        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.BLACK)
                    textView.setTypeface(null, Typeface.BOLD)
                    textView.textSize = 16f
                    snackbar.show()
                }

                val mealIngListWithoutNulls: List<String>? = allMeals[index].ingredients?.toList()
                val mealMeListWithoutNulls: List<String>? = allMeals[index].measure?.toList()

                val filteredList_ing = ArrayList<String>()
                val filteredList_me = ArrayList<String>()

                if (mealIngListWithoutNulls != null) {
                    for (index in 0 until mealIngListWithoutNulls.size){
                        if (mealIngListWithoutNulls[index].isNotEmpty() && mealIngListWithoutNulls[index]!= "  " &&  mealIngListWithoutNulls[index]!= "   "){
                            filteredList_ing.add(mealIngListWithoutNulls.get(index))
                        }
                        if (mealMeListWithoutNulls?.get(index)?.isNotEmpty()!! &&  mealIngListWithoutNulls[index]!= "  " &&  mealIngListWithoutNulls[index]!= "   "){
                            filteredList_me.add(mealMeListWithoutNulls.get(index))
                        }
                    }
                }

                for (index in 0 until filteredList_ing.size) {
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