package com.quizzical.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quizzical.R
import com.quizzical.enums.DifficultyLevels
import com.quizzical.viewholders.DifficultyVh
import com.quizzical.viewholders.OnDifficultyClickListener

class DifficultyAdapter(onDifficultyClickListener: OnDifficultyClickListener) :
    RecyclerView.Adapter<DifficultyVh>() {

    val difficultyLevels = DifficultyLevels.values()
    private var onDifficultyListener = onDifficultyClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DifficultyVh {
        val root = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.difficulty_item, parent, false
        )
        return DifficultyVh(root, onDifficultyListener)
    }

    override fun getItemCount(): Int = difficultyLevels.size

    override fun onBindViewHolder(holder: DifficultyVh, position: Int) {
        holder.bind(difficultyLevels[position])
    }
}