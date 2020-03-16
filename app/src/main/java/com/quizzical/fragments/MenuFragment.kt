package com.quizzical.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.adapters.MenuAdapter
import com.quizzical.enums.FragmentTypes
import com.quizzical.models.FragmentData
import com.quizzical.viewholders.OnCategoryClickListener
import com.quizzical.viewmodels.FragmentVm
import com.quizzical.viewmodels.MenuVm

class MenuFragment : Fragment(), OnCategoryClickListener {

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

    private lateinit var fragmentVm: FragmentVm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        ButterKnife.bind(this, root)
        setupFragmentVm()
        setupCategoriesRv()
        return root
    }

    private fun setupFragmentVm() {
        fragmentVm = activity?.run {
            ViewModelProviders.of(this)[FragmentVm::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private fun setupCategoriesRv() {
        menuAdapter = MenuAdapter(this)
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

    override fun onCategorySelected(position: Int) {
        Toast.makeText(
            context,
            menuVm.triviaCategories.value?.get(position)?.name,
            Toast.LENGTH_SHORT
        ).show()
        fragmentVm.currentFragment.value = FragmentData(FragmentTypes.DifficultyFragment)
    }
}