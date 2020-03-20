package com.quizzical.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.enums.FragmentTypes
import com.quizzical.models.FragmentData
import com.quizzical.models.Question
import com.quizzical.viewmodels.FragmentVm
import com.quizzical.viewmodels.QuestionsVm


class QuestionFragment : Fragment() {

    @BindView(R.id.title)
    lateinit var title: TextView

    @BindView(R.id.question)
    lateinit var questionTv: TextView

    @BindView(R.id.multiple_choice_options)
    lateinit var multipleChoiceOptions: LinearLayout

    private lateinit var questionsVm: QuestionsVm
    private lateinit var fragmentVm: FragmentVm

    private lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        ButterKnife.bind(this, view)
        setupQuestionVm()
        setupFragmentVm()
        arguments?.getInt("current_question_index")?.let {
            loadQuestion(it)
        }
        return view
    }

    private fun setupFragmentVm() {
        activity?.let {
            fragmentVm = ViewModelProviders.of(it).get(FragmentVm::class.java)
        }
    }

    private fun setupQuestionVm() {
        activity?.let {
            questionsVm = ViewModelProviders.of(it).get(QuestionsVm::class.java)
        }
    }

    private fun loadQuestion(questionIndex: Int) {
        questionsVm.triviaQuestions.value?.get(questionIndex)?.let {
            title.text = String.format(getString(R.string.question_number), questionIndex + 1)
            question = it
            question.let {
                questionTv.text = decodeBase64(question.question)
                val correctAnswer = decodeBase64(question.correct_answer)
                val incorrectAnswers = decodeBase64List(question.incorrect_answers)
                val options: ArrayList<String> = ArrayList()
                options.add(correctAnswer)
                options.addAll(incorrectAnswers)
                options.shuffle()
                createOptionsList(options, correctAnswer, questionIndex)
            }
        }
    }

    private fun decodeBase64List(list: List<String>): Collection<String> {
        val collection = ArrayList<String>()
        for (i in list.indices) {
            decodeBase64(list[i])?.let {
                collection.add(it)
            }
        }
        return collection
    }

    private fun decodeBase64(coded: String): String {
        val data: ByteArray = Base64.decode(coded, Base64.DEFAULT)
        return String(data)
    }

    private fun createOptionsList(
        options: ArrayList<String>,
        correctAnswer: String,
        questionIndex: Int
    ) {
        options.forEach { option ->
            val row = layoutInflater.inflate(
                R.layout.option_item,
                multipleChoiceOptions,
                false
            )
            val optionTextView = row.findViewById<TextView>(R.id.option_text)
            optionTextView.text = option
            row.setOnClickListener {
                if (option == correctAnswer) {
                    optionTextView.setTextColor(Color.WHITE)
                    row.setBackgroundColor(Color.GREEN)
                    val bundle = Bundle()
                    bundle.putInt(
                        "current_question_index",
                        questionIndex + 1
                    )
                    Handler().postDelayed({
                        fragmentVm.currentFragment.value =
                            FragmentData(FragmentTypes.QuestionFragment, bundle, false)
                    }, 1000)
                } else {
                    optionTextView.setTextColor(Color.WHITE)
                    row.setBackgroundColor(Color.RED)
                    Handler().postDelayed({
                        fragmentVm.currentFragment.value =
                            FragmentData(FragmentTypes.LoseFragment)
                    }, 1000)
                }
            }
            multipleChoiceOptions.addView(row)
        }
    }
}