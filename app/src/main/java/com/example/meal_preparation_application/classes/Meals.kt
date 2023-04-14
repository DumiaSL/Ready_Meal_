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
        val output =
            "\"Meal\" : \"$name\",\n"+
            "\"Meal\" : \"$drinkAlternate\",\n"+
            "\"Meal\" : \"$category\",\n"+
            "\"Meal\" : \"$area\",\n"+
            "\"Meal\" : \"$instructions\",\n"+
            "\"Meal\" : \"$mealThumb\",\n"+
            "\"Meal\" : \"$tags\",\n"+
            "\"Meal\" : \"$youtube\",\n"+
            "\"Meal\" : \"$source\",\n"+
            "\"Meal\" : \"$imageSource\",\n"

//            "\"Meal\" : \"${ingredients?.get(0)}\",\n"+
//            "\"Meal\" : \"${ingredients?.get(0)}\",\n"+
//            "\"Meal\" : \"${ingredients?.get(0)}\",\n"+
//            "\"Meal\" : \"${ingredients?.get(0)}\",\n"+
//            "\"Meal\" : \"${ingredients?.get(0)}\",\n"+
//            "\"Meal\" : \"${ingredients?.get(0)}\",\n"+
//            "\"Meal\" : \"$name\",\n"+
//            "\"Meal\" : \"$name\",\n"+
//            "\"Meal\" : \"$name\",\n"+
//            "\"Meal\" : \"$name\",\n"+
//        "Meal":"Chicken Alfredo Primavera",
//        "DrinkAlternate":null,
//        "Category":"Chicken",
//        "Area":"Italian",
//        "Instructions":"Heat 1 tablespoon of butter and 2 tablespoons of .... ",
//        "Tags":"Pasta,Meat,Dairy",
//        "Youtube":"https:\/\/www.youtube.com\/watch?v=qCIbq8HywpQ",
//        "Ingredient1":"Butter",
//        3
//        "Ingredient2":"Olive Oil",
//        "Ingredient3":"Chicken",
//        "Ingredient4":"Salt",
//        "Ingredient5":"Squash",
//        "Ingredient6":"Broccoli",
//        "Ingredient7":"mushrooms",
//        "Ingredient8":"Pepper",
//        "Ingredient9":"onion",
//        "Ingredient10":"garlic",
//        "Ingredient11":"red pepper flakes",
//        "Ingredient12":"white wine",
//        "Ingredient13":"milk",
//        "Ingredient14":"heavy cream",
//        "Ingredient15":"Parmesan cheese",
//        "Ingredient16":"bowtie pasta",
//        "Ingredient17":"Salt",
//        "Ingredient18":"Pepper",
//        "Ingredient19":"Parsley",
//        "Ingredient20":"",
//        "Measure1":"2 tablespoons",
//        "Measure2":"3 tablespoons",
//        "Measure3":"5 boneless",
//        "Measure4":"1 teaspoon",
//        "Measure5":"1 cut into 1\/2-inch cubes",
//        "Measure6":"1 Head chopped",
//        "Measure7":"8-ounce sliced",
//        "Measure8":"1 red",
//        "Measure9":"1 chopped",
//        "Measure10":"3 cloves",
//        "Measure11":"1\/2 teaspoon",
//        "Measure12":"1\/2 cup",
//        "Measure13":"1\/2 cup",
//        "Measure14":"1\/2 cup",
//        "Measure15":"1 cup grated",
//        "Measure16":"16 ounces",
//        "Measure17":"pinch",
//        "Measure18":"pinch ",
//        "Measure19":"chopped",
//        "Measure20":"",
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




