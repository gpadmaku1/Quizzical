package com.quizzical.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.activities.MainActivity
import com.quizzical.enums.FragmentTypes
import com.quizzical.models.FragmentData
import com.quizzical.viewmodels.FragmentVm

class WinFragment : Fragment() {

    @BindView(R.id.menu_button)
    lateinit var menuButton: AppCompatButton

    private lateinit var fragmentVm: FragmentVm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_win, container, false)
        ButterKnife.bind(this, view)
        setupFragmentVm()
        setupMenuButton()
        setHighScore()
        return view
    }

    private fun setHighScore() {
        val sharedPref =
            activity?.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE) ?: return
        arguments?.getInt("current_score")?.let {
            with(sharedPref.edit()) {
                putInt(MainActivity.HIGH_SCORE_SP_KEY, it)
                commit()
            }
        }
    }

    private fun setupFragmentVm() {
        activity?.let {
            fragmentVm = ViewModelProviders.of(it).get(FragmentVm::class.java)
        }
    }

    private fun setupMenuButton() {
        menuButton.setOnClickListener {
            fragmentVm.currentFragment.value =
                FragmentData(FragmentTypes.DifficultyFragment, Bundle.EMPTY)
        }
    }
}