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
import com.quizzical.viewmodels.FragmentVm
import com.quizzical.viewmodels.QuestionsVm

class MainActivity : FragmentActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
        const val QUESTION_INDEX_BUNDLE_KEY = "current_question_index"
        const val PREFS_NAME = "QuizzicalSharedPreferencesFile"
        const val HIGH_SCORE_SP_KEY = "quizzical_high_score"
    }

    @BindView(R.id.fragment_container)
    lateinit var fragmentContainer: FrameLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private val fragmentVm: FragmentVm by lazy { FragmentVm.get(this) }
    private val questionsVm: QuestionsVm by lazy { QuestionsVm.get(this) }

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
                DifficultyFragment().apply {
                    arguments = bundle
                }
            }
            FragmentTypes.LoseFragment -> {
                LoseFragment().apply {
                    arguments = bundle
                }
            }
            FragmentTypes.WinFragment -> {
                WinFragment().apply {
                    arguments = bundle
                }
            }
            FragmentTypes.QuestionFragment -> {
                if (!bundle.isEmpty) {
                    QuestionFragment().apply {
                        val currentQuestionIndex =
                            bundle.getInt(QUESTION_INDEX_BUNDLE_KEY)
                        questionsVm.triviaQuestions.value?.size?.let {
                            if (currentQuestionIndex < it) {
                                val packageData = Bundle()
                                packageData.putInt(
                                    QUESTION_INDEX_BUNDLE_KEY,
                                    currentQuestionIndex
                                )
                                arguments = packageData
                            } else {
                                val highScoreBundle = Bundle()
                                highScoreBundle.putInt("current_score", it)
                                fragmentVm.currentFragment.value =
                                    FragmentData(FragmentTypes.WinFragment, highScoreBundle)
                            }
                        }
                    }
                } else {
                    QuestionFragment().apply {
                        val packageData = Bundle()
                        packageData.putInt(QUESTION_INDEX_BUNDLE_KEY, 0)
                        arguments = packageData
                    }
                }
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
