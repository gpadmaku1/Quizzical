package com.quizzical.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.fragments.MenuFragment

class MainActivity : FragmentActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    @BindView(R.id.fragment_container)
    lateinit var fragmentContainer: FrameLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        initialize()
    }

    private fun initialize() {
        val fragment = MenuFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }
}
