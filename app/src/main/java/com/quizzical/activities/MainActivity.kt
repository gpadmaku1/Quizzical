package com.quizzical.activities

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.enums.FragmentTypes
import com.quizzical.fragments.DifficultyFragment
import com.quizzical.fragments.LoseFragment
import com.quizzical.fragments.QuestionFragment
import com.quizzical.fragments.WinFragment
import com.quizzical.models.FragmentData
import com.quizzical.models.Question
import com.quizzical.viewmodels.FragmentVm
import com.quizzical.viewmodels.QuestionsVm

class MainActivity : FragmentActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    @BindView(R.id.fragment_container)
    lateinit var fragmentContainer: FrameLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private val fragmentVm: FragmentVm by lazy { FragmentVm.get(this) }
    private val questionsVm: QuestionsVm by lazy { QuestionsVm.get(this) }

    private lateinit var questions: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        initDifficultyFragment()
        setupFragmentVm()
    }

    private fun setupFragmentVm() {
        fragmentVm.currentFragment.observe(this, Observer { fragmentData ->
            Log.d(TAG, fragmentData.fragmentTypes.name)
            replaceCurrentFragment(
                fragmentData.fragmentTypes,
                fragmentData.bundle,
                fragmentData.addToBackStack
            )
        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    private fun replaceCurrentFragment(
        fragmentType: FragmentTypes,
        bundle: Bundle,
        addToBackStack: Boolean
    ) {
        val fragment = when (fragmentType) {
            FragmentTypes.DifficultyFragment -> {
                DifficultyFragment()
            }
            FragmentTypes.QuestionFragment -> {
                if (!bundle.isEmpty) {
                    val currentQuestionIndex =
                        bundle.getInt("current_question_index")
                    QuestionFragment().apply {
                        questionsVm.triviaQuestions.value?.size?.let {
                            if (currentQuestionIndex < it) {
                                val packageData = Bundle()
                                packageData.putInt(
                                    "current_question_index",
                                    currentQuestionIndex
                                )
                                arguments = packageData
                            } else {
                                fragmentVm.currentFragment.value =
                                    FragmentData(FragmentTypes.WinFragment, Bundle.EMPTY)
                            }
                        }
                    }
                } else {
                    QuestionFragment().apply {
                        val packageData = Bundle()
                        packageData.putInt("current_question_index", 0)
                        arguments = packageData
                    }
                }
            }
            FragmentTypes.LoseFragment -> {
                LoseFragment().apply {
                    arguments = bundle
                }
            }
            FragmentTypes.WinFragment -> {
                WinFragment()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).apply {
            if (addToBackStack) {
                addToBackStack(null)
            }
        }.also {
            it.commit()
        }
    }

    private fun initDifficultyFragment() {
        val fragment = DifficultyFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
    }
}
