package com.quizzical.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R

class MenuFragment : Fragment() {

    companion object {
        val LOG: String = MenuFragment::class.java.simpleName
    }

    @BindView(R.id.category_rv)
    lateinit var categoryRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        ButterKnife.bind(this, view)
        setupCategoriesRv()
        return view
    }

    private fun setupCategoriesRv() {
        
    }
}