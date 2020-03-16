package com.quizzical.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.models.Category

class MenuVh(itemView: View, onCategoryClickListener: OnCategoryClickListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    @BindView(R.id.category_name)
    lateinit var categoryName: TextView

    private var listener: OnCategoryClickListener = onCategoryClickListener

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bind(category: Category) {
        categoryName.text = category.name
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        listener.onCategorySelected(adapterPosition)
    }
}

interface OnCategoryClickListener {
    fun onCategorySelected(position: Int)
}