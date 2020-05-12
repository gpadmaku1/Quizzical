package com.quizzical.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.activities.MainActivity
import com.quizzical.adapters.DifficultyAdapter
import com.quizzical.enums.FragmentTypes
import com.quizzical.models.FragmentData
import com.quizzical.viewholders.OnDifficultyClickListener
import com.quizzical.viewmodels.FragmentVm
import com.quizzical.viewmodels.QuestionsVm


class DifficultyFragment : Fragment(), OnDifficultyClickListener {
    companion object {
        val TAG: String = DifficultyFragment::class.java.simpleName
    }

    @BindView(R.id.difficulty_rv)
    lateinit var difficultyRecyclerView: RecyclerView

    @BindView(R.id.lottie_loading_view)
    lateinit var lottieView: ConstraintLayout

    @BindView(R.id.lottie_no_internet_view)
    lateinit var noInternetView: ConstraintLayout

    @BindView(R.id.mainLayout)
    lateinit var mainLayout: ConstraintLayout

    @BindView(R.id.high_score_tv)
    lateinit var highScore: TextView

    private lateinit var questionsVm: QuestionsVm
    private lateinit var fragmentVm: FragmentVm

    private lateinit var difficultyAdapter: DifficultyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_difficulty, container, false)
        ButterKnife.bind(this, view)
        setupQuestionVm()
        setupFragmentVm()
        setupHighScoreTv()
        setupDifficultyRv()
        return view
    }

    private fun setupQuestionVm() {
        activity?.let {
            questionsVm = ViewModelProviders.of(it).get(QuestionsVm::class.java)
        }
    }

    private fun setupFragmentVm() {
        activity?.let {
            fragmentVm = ViewModelProviders.of(it).get(FragmentVm::class.java)
        }
    }

    private fun setupHighScoreTv() {
        val sharedPref =
            activity?.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref) {
            getInt(MainActivity.HIGH_SCORE_SP_KEY, 0)
        }.also {
            highScore.text =
                String.format(getString(R.string.high_score), it)
        }

    }

    private fun setupDifficultyRv() {
        difficultyAdapter = DifficultyAdapter(this)
        difficultyRecyclerView.apply {
            adapter = difficultyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDifficultySelected(position: Int) {
        lottieView.visibility = View.VISIBLE
        val difficultyLevel = difficultyAdapter.difficultyLevels[position].urlParam
        if (hasNetworkAvailable()) {
            questionsVm.fetchQuestions(difficultyLevel)
            questionsVm.triviaQuestions.observe(this, Observer {
                if (it.isNotEmpty()) {
                    lottieView.visibility = View.GONE
                    fragmentVm.currentFragment.value =
                        FragmentData(FragmentTypes.QuestionFragment, Bundle.EMPTY)
                } else {
                    lottieView.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Failed to fetch questions from Internet. Check connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            mainLayout.visibility = View.GONE
            noInternetView.visibility = View.VISIBLE
        }
    }

    private fun hasNetworkAvailable(): Boolean {
        context?.let {
            val service = Context.CONNECTIVITY_SERVICE
            val manager = it.getSystemService(service) as ConnectivityManager?
            val network = manager?.activeNetworkInfo
            Log.d(TAG, "hasNetworkAvailable: ${(network != null)}")
            return (network != null)
        }
        return false
    }

}