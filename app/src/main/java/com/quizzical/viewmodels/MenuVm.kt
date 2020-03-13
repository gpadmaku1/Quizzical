package com.quizzical.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.quizzical.fragments.MenuFragment
import com.quizzical.models.Category
import com.quizzical.models.TriviaCategories
import com.quizzical.utils.NetworkUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuVm : ViewModel() {

    companion object {
        val TAG: String = MenuVm::class.java.simpleName
        const val categoryUrl = "https://opentdb.com/api_category.php"

        fun get(fragment: MenuFragment) = ViewModelProviders.of(fragment).get(MenuVm::class.java)
    }

    val triviaCategories = MutableLiveData<List<Category>>()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val stringResponse = NetworkUtils.getRequest(categoryUrl)
            if (stringResponse != null) {
                val jsonAdapter: JsonAdapter<TriviaCategories> = moshi.adapter(TriviaCategories::class.java)
                val categoryList = jsonAdapter.fromJson(stringResponse)?.trivia_categories
                launch(Dispatchers.Main) {
                    triviaCategories.value = categoryList
                }
            }
        }
    }
}