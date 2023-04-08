package com.example.meal_preparation_application

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginTop

class Search_By_Ingredient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_ingredient)

        val cardScroll = findViewById<LinearLayout>(R.id.scroll_layout)

        val layoutCount = 10 // number of LinearLayouts to create

        for (i in 1..layoutCount) {
            val linearLayout = LinearLayout(this) // create a new LinearLayout
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ) // set layout params
            linearLayout.setPadding(20,20,20,0)
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
                    setMargins(10,20 , 10, 30)
                }
                contentDescription = context.getString(R.string.app_name)
                setImageResource(R.drawable.foodtest)
            }

            val mealName = TextView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(150, 20, 0, 20)
                typeface = ResourcesCompat.getFont(context, R.font.poppins_bold)
                text = "Vegetable Heart Dish"
                setTextColor(Color.parseColor("#0E0E0E"))
                textSize = 26f // 26sp in pixels
                setTypeface(typeface, Typeface.BOLD)
            }

            val mealCategory = TextView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(300, 20, 0, 20)
                typeface = ResourcesCompat.getFont(context, R.font.poppins_bold)
                text = "Category : " + "Chicken"
                setTextColor(Color.parseColor("#0E0E0E"))
                textSize = 16f // 26sp in pixels
                setTypeface(typeface, Typeface.BOLD)
            }

            val mealArea = TextView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(300, 20, 0, 40)
                typeface = ResourcesCompat.getFont(context, R.font.poppins_bold)
                text = "Area         : " + "Sri Lanka"
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
            button.text = "Save meals to Database"
            button.setTextColor(Color.parseColor("#0E0E0E"))
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            button.setTypeface(button.typeface, Typeface.BOLD)

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
}