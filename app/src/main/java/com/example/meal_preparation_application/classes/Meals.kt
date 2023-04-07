package com.example.meal_preparation_application.classes

import androidx.room.*

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
//    @TypeConverters(StringListTypeConverter::class)
    @ColumnInfo(name = "ingredients") var ingredients: ArrayList<String>? = null,
//    @TypeConverters(StringListTypeConverter::class)
//    @ColumnInfo(name = "measure")var measure: List<String>? = null,
//    var ingredients: String? = null,
//    var measure: String? = null,
    @ColumnInfo(name = "tags") var tags: String? = null,
    @ColumnInfo(name = "youtube") var youtube: String? = null,
    @ColumnInfo(name = "source") var source: String? = null,
    @ColumnInfo(name = "imageSource") var imageSource: String? = null,
    @ColumnInfo(name = "creativeCommonsConfirmed") var creativeCommonsConfirmed: String? = null,
    @ColumnInfo(name = "dateModified") var dateModified: String? = null
)

class StringListTypeConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun toString(value: List<String>?): String? {
        return value?.joinToString(",")
    }
}


