package com.quizzical.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.viewmodels.QuestionsVm

class QuestionFragment : Fragment() {

    companion object {
        val TAG: String = QuestionFragment::class.java.simpleName
    }

    @BindView(R.id.title)
    lateinit var title: TextView

    @BindView(R.id.question)
    lateinit var questionTv: TextView

    @BindView(R.id.multiple_choice_options)
    lateinit var multipleChoiceOptions: LinearLayout

    private lateinit var questionsVm: QuestionsVm
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        ButterKnife.bind(this, view)
        setupQuestionVm()
        arguments?.getInt(context?.getString(R.string.current_question_key))?.let {
            loadQuestion(it)
        }
        return view
    }

    private fun setupQuestionVm() {
        activity?.let {
            questionsVm = ViewModelProviders.of(it).get(QuestionsVm::class.java)
        }
    }

    private fun loadQuestion(questionIndex: Int) {
        questionsVm.triviaQuestions.value?.get(questionIndex)?.let { question ->
            title.text = String.format(getString(R.string.question_number), questionIndex)
            question.let {
                questionTv.text = question.question
                val correctAnswer = question.correct_answer
                val incorrectAnswers = question.incorrect_answers
                val options: ArrayList<String> = ArrayList()
                options.add(correctAnswer)
                options.addAll(incorrectAnswers)
                options.shuffle()
                createOptionsList(options, correctAnswer)
            }
        }
    }

    private fun createOptionsList(
        options: ArrayList<String>,
        correctAnswer: String
    ) {
        options.forEach { option ->
            val row = layoutInflater.inflate(
                R.layout.option_item,
                multipleChoiceOptions,
                false
            )
            row.findViewById<TextView>(R.id.option_text).text = option
            row.setOnClickListener {
                if (option == correctAnswer) {
                    Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Wrong!", Toast.LENGTH_SHORT).show()
                }
            }
            multipleChoiceOptions.addView(row)
        }
    }
}