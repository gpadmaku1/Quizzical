package com.quizzical.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setupDifficultyRv()
        return view
    }

    private fun setupQuestionVm() {
        activity?.let {
            questionsVm = ViewModelProviders.of(it).get(QuestionsVm::class.java)
        }
    }

    private fun setupDifficultyRv() {
        difficultyAdapter = DifficultyAdapter(this)
        difficultyRecyclerView.apply {
            adapter = difficultyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupFragmentVm() {
        activity?.let {
            fragmentVm = ViewModelProviders.of(it).get(FragmentVm::class.java)
        }
    }

    override fun onDifficultySelected(position: Int) {
        lottieView.visibility = View.VISIBLE
        val difficultyLevel = difficultyAdapter.difficultyLevels[position].urlParam
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
    }
}