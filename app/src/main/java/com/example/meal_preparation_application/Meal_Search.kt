package com.example.meal_preparation_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class Meal_Search : AppCompatActivity() {
    lateinit var searchBar:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_search)

        var  search_word= intent.getStringExtra("search_word").toString()
        searchBar = findViewById(R.id.search_bar)
        var backIcon = findViewById<TextView>(R.id.backButton)

        //ini
        searchBar.setText(search_word)

        backIcon.setOnClickListener {
            this.finish()
        }


    }
}