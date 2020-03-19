package com.quizzical.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.quizzical.activities.MainActivity
import com.quizzical.models.Question
import com.quizzical.models.TriviaQuestions
import com.quizzical.utils.NetworkUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionsVm : ViewModel() {
    companion object {
        val TAG: String = QuestionsVm::class.java.simpleName
        var questionsUrl =
            "https://opentdb.com/api.php?amount=50&type=multiple&difficulty="

        fun get(activity: MainActivity) =
            ViewModelProviders.of(activity).get(QuestionsVm::class.java)
    }

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val triviaQuestions = MutableLiveData<List<Question>>()

    fun getTriviaQuestions(): List<Question>? {
        return triviaQuestions.value
    }

    fun fetchQuestions(difficultyLevel: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val stringResponse = NetworkUtils.getRequest(questionsUrl + difficultyLevel)
            if (stringResponse != null) {
                val jsonAdapter: JsonAdapter<TriviaQuestions> =
                    moshi.adapter(TriviaQuestions::class.java)
                val questions = jsonAdapter.fromJson(stringResponse)?.results
                launch(Dispatchers.Main) {
                    triviaQuestions.value = questions
                }
            }
        }
    }

}