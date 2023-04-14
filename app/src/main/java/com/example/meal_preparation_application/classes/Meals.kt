package com.example.meal_preparation_application.classes

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "meals")
data class Meals(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "drinkAlternate") var drinkAlternate: String? = null,
    @ColumnInfo(name = "category") var category: String? = null,
    @ColumnInfo(name = "area") var area: String? = null,
    @ColumnInfo(name = "instructions") var instructions: String? = null,
    @ColumnInfo(name = "mealThumb") var mealThumb: String? = null,
    @TypeConverters(StringListTypeConverter::class)
    @ColumnInfo(name = "ingredients") var ingredients: ArrayList<String>? = null,
    @TypeConverters(StringListTypeConverter::class)
    @ColumnInfo(name = "measure") var measure: ArrayList<String>? = null,
    @ColumnInfo(name = "tags") var tags: String? = null,
    @ColumnInfo(name = "youtube") var youtube: String? = null,
    @ColumnInfo(name = "source") var source: String? = null,
    @ColumnInfo(name = "imageSource") var imageSource: String? = null,
    @ColumnInfo(name = "creativeCommonsConfirmed") var creativeCommonsConfirmed: String? = null,
    @ColumnInfo(name = "dateModified") var dateModified: String? = null
){
    override fun toString(): String {
        var output =
            "\"Meal\" : \"$name\",\n"+
            "\"DrinkAlternate\" : \"$drinkAlternate\",\n"+
            "\"Category\" : \"$category\",\n"+
            "\"Area\" : \"$area\",\n"+
            "\"Instructions\" : \"$instructions\",\n"+
            "\"MealThumbnail\" : \"$mealThumb\",\n"+
            "\"Tags\" : \"$tags\",\n"+
            "\"Youtube\" : \"$youtube\",\n"+
            "\"Source\" : \"$source\",\n"+
            "\"imageSource\" : \"$imageSource\",\n"

        var formattedIngredients = ""
        var formattedMeasure = ""
        var count = 1
        for (i in 0 until (ingredients?.size!!)) {
            if (ingredients!![i] != ""){
                formattedIngredients += ("\"Ingredient\"" + (count) + " : \"" + ingredients!!.get(i)) + "\"\n"
                formattedMeasure += ("\"Measure\"" + (count) + " : \"" + measure!!.get(i)) + "\"\n"
                count++
            }
        }
        output+=formattedIngredients+formattedMeasure + "\"CreativeCommonsConfirmed\" : \"$creativeCommonsConfirmed\",\n" +
                "\"DateModified\" : \"$dateModified\",\n"
        return output
    }
}

class StringListTypeConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: ArrayList<String?>): String {
        return Gson().toJson(value)
    }
}




