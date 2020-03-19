package com.quizzical.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        fragmentVm = activity?.run {
            ViewModelProviders.of(this)[FragmentVm::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onDifficultySelected(position: Int) {
        val difficultyLevel = difficultyAdapter.difficultyLevels[position].urlParam
        questionsVm.fetchQuestions(difficultyLevel)
        questionsVm.triviaQuestions.observe(this, Observer {
            if (it.isNotEmpty()) {
                fragmentVm.currentFragment.value =
                    FragmentData(FragmentTypes.QuestionFragment)
            } else {
                Toast.makeText(
                    context,
                    "Failed to fetch questions from Internet. Check connection.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}