package com.quizzical.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.quizzical.R
import com.quizzical.enums.DifficultyLevels

class DifficultyVh(itemView: View, onDifficultyClickListener: OnDifficultyClickListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    @BindView(R.id.difficulty_name)
    lateinit var difficultyName: TextView

    private var listener: OnDifficultyClickListener = onDifficultyClickListener

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bind(difficultyLevels: DifficultyLevels) {
        difficultyName.text = difficultyLevels.name
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        listener.onDifficultySelected(adapterPosition)
    }
}

interface OnDifficultyClickListener {
    fun onDifficultySelected(position: Int)
}