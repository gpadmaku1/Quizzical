package com.quizzical.models

import java.io.Serializable

data class Question(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
) : Serializable
