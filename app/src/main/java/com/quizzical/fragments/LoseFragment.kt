package com.quizzical.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.enums.FragmentTypes
import com.quizzical.models.FragmentData
import com.quizzical.viewmodels.FragmentVm

class LoseFragment : Fragment() {

    @BindView(R.id.user_score)
    lateinit var userScore: TextView

    @BindView(R.id.menu_button)
    lateinit var menuButton: AppCompatButton

    private lateinit var fragmentVm: FragmentVm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lose, container, false)
        ButterKnife.bind(this, view)
        setupFragmentVm()
        setupHighScoreText()
        setupMenuButton()
        return view
    }

    private fun setupHighScoreText() {
        arguments?.getInt("current_score")?.let {
            userScore.text = String.format(getString(R.string.session_score), it)
        }
    }

    private fun setupMenuButton() {
        menuButton.setOnClickListener {
            fragmentVm.currentFragment.value =
                FragmentData(FragmentTypes.DifficultyFragment, Bundle.EMPTY)
        }
    }

    private fun setupFragmentVm() {
        activity?.let {
            fragmentVm = ViewModelProviders.of(it).get(FragmentVm::class.java)
        }
    }
}