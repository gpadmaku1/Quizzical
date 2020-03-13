package com.quizzical.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.viewmodels.MenuVm

class MenuFragment : Fragment() {

    companion object {
        val TAG: String = MenuFragment::class.java.simpleName
    }

    @BindView(R.id.category_rv)
    lateinit var categoryRecyclerView: RecyclerView

    @BindView(R.id.clickButton)
    lateinit var clickButton: Button

    private val menuVm by lazy { MenuVm.get(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        ButterKnife.bind(this, root)
        setupCategoriesRv()
        return root
    }

    private fun setupCategoriesRv() {
        menuVm.fetchCategories()
        menuVm.triviaCategories.observe(this, Observer {
            it.forEach {
                Log.d(TAG, "name: ${it.name} + id: ${it.id}")
            }
        })
    }
}