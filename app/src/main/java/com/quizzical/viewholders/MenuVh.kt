package com.quizzical.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.models.Category

class MenuVh(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.category_name)
    lateinit var categoryName: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bind(category: Category) {
        categoryName.text = category.name
    }
}