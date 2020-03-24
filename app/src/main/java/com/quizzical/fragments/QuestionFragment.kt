package com.quizzical.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.activities.MainActivity.Companion.QUESTION_INDEX_BUNDLE_KEY
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

    @BindView(R.id.lottie_checkmark_view)
    lateinit var checkMarkView: FrameLayout

    @BindView(R.id.lottie_cross_view)
    lateinit var crossView: FrameLayout

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
        arguments?.getInt(QUESTION_INDEX_BUNDLE_KEY)?.let {
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
        val rows = ArrayList<View>()
        options.forEach { option ->
            val row = layoutInflater.inflate(
                R.layout.option_item,
                multipleChoiceOptions,
                false
            )
            rows.add(row)
            val optionTextView = row.findViewById<TextView>(R.id.option_text)
            optionTextView.text = option
            row.setOnClickListener {
                if (option == correctAnswer) {
                    optionTextView.setTextColor(Color.WHITE)
                    context?.let {
                        row.setBackgroundColor(ContextCompat.getColor(it, R.color.green_correct))
                    }
                    val bundle = Bundle().apply {
                        putInt(QUESTION_INDEX_BUNDLE_KEY, questionIndex + 1)
                    }
                    Handler().postDelayed({ checkMarkView.visibility = View.VISIBLE }, 500)
                    Handler().postDelayed({
                        checkMarkView.visibility = View.GONE
                        fragmentVm.currentFragment.value =
                            FragmentData(FragmentTypes.QuestionFragment, bundle)
                    }, 3000)
                } else {
                    optionTextView.setTextColor(Color.WHITE)
                    context?.let { context ->
                        row.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.red_incorrect
                            )
                        )
                        updateRowColors(rows, correctAnswer, context)
                    }

                    Handler().postDelayed({ crossView.visibility = View.VISIBLE }, 500)
                    Handler().postDelayed({
                        val bundle = Bundle().apply {
                            putInt("current_score", questionIndex)
                        }
                        crossView.visibility = View.GONE
                        fragmentVm.currentFragment.value =
                            FragmentData(FragmentTypes.LoseFragment, bundle)
                    }, 3000)
                }
            }
            multipleChoiceOptions.addView(row)
        }
    }

    private fun updateRowColors(
        rows: ArrayList<View>,
        correctAnswer: String,
        context: Context
    ) {
        rows.forEach { view ->
            val rowOptionTextView = view.findViewById<TextView>(R.id.option_text)
            if (rowOptionTextView.text != correctAnswer) {
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red_incorrect
                    )
                )
                rowOptionTextView.setTextColor(Color.WHITE)
            } else {
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green_correct
                    )
                )
                rowOptionTextView.setTextColor(Color.WHITE)
            }
        }
    }
}