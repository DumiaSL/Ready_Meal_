//package com.example.meal_preparation_application.classes
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//
//class MealViewModel(application: Application) : AndroidViewModel(application) {
//    private val repository: MealViewModel
//    // LiveData gives us updated words when they change.
//    val allWords: LiveData<List<Meal>>
//
//    init {
//        val dao = DatabaseService.getDatabase(application).Dao()
//        repository = MealViewModel(dao)
//        allWords = repository.allWords
//    }
//
//    fun insert(word: Word) {
//        viewModelScope.launch {
//            repository.insert(word)
//
//        }
//    }
//
//    fun delete() {
//        viewModelScope.launch {
//            repository.deleteAll()
//        }
//    }
//
//    fun deleteWord(deleteWord: String) {
//        viewModelScope.launch {
//            repository.delete(deleteWord)
//        }
//    }
//
//
//}