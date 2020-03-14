package com.quizzical.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quizzical.R
import com.quizzical.models.Category
import com.quizzical.viewholders.MenuVh

class MenuAdapter : RecyclerView.Adapter<MenuVh>() {

    private var triviaCategories = emptyList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuVh {
        val root = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.category_item, parent, false
        )
        return MenuVh(root)
    }

    override fun getItemCount(): Int = triviaCategories.size

    override fun onBindViewHolder(holder: MenuVh, position: Int) {
        holder.bind(triviaCategories[position])
    }

    fun displayCategories(triviaCategories: List<Category>) {
        this.triviaCategories = triviaCategories
        notifyDataSetChanged()
    }

    fun isEmpty() = triviaCategories.isEmpty()
}