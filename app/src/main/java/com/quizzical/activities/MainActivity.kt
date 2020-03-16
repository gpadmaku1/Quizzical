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
import com.quizzical.fragments.MenuFragment
import com.quizzical.viewmodels.FragmentVm

class MainActivity : FragmentActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    @BindView(R.id.fragment_container)
    lateinit var fragmentContainer: FrameLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private val fragmentVm: FragmentVm by lazy { FragmentVm.get(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        initializeMenuFragment()
        setupFragmentVm()
    }

    private fun setupFragmentVm() {
        fragmentVm.currentFragment.observe(this, Observer { fragmentData ->
            Log.d(TAG, fragmentData.fragmentTypes.name)
            replaceCurrentFragment(fragmentData.fragmentTypes)
        })
    }

    private fun replaceCurrentFragment(fragmentType: FragmentTypes) {
        val fragment = when (fragmentType) {
            FragmentTypes.MenuFragment -> MenuFragment.getInstance()
            FragmentTypes.DifficultyFragment -> DifficultyFragment.getInstance()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun initializeMenuFragment() {
        val fragment = MenuFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }
}
