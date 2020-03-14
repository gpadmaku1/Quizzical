package com.quizzical.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.adapters.MenuAdapter
import com.quizzical.viewmodels.MenuVm

class MenuFragment : Fragment() {

    companion object {
        val TAG: String = MenuFragment::class.java.simpleName

        fun getInstance(): MenuFragment = MenuFragment()
    }

    @BindView(R.id.category_rv)
    lateinit var categoryRecyclerView: RecyclerView

    @BindView(R.id.progress_circular)
    lateinit var progressBar: ProgressBar

    private val menuVm by lazy { MenuVm.get(this) }

    private var menuLayoutManager = LinearLayoutManager(context)
    private lateinit var menuAdapter: MenuAdapter

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
        menuAdapter = MenuAdapter()
        categoryRecyclerView.apply {
            layoutManager = menuLayoutManager
            adapter = menuAdapter
        }
        if (menuAdapter.isEmpty()) {
            menuVm.fetchCategories()
        }
        menuVm.triviaCategories.observe(this, Observer {
            progressBar.visibility = View.GONE
            menuAdapter.displayCategories(it)
        })
    }
}