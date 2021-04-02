package com.example.hakta_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hakta_app.R
import com.example.hakta_app.models.Quest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_best_quest.view.*

class BestQuestsAdapter(val quests: List<Quest>)
    : RecyclerView.Adapter<BestQuestsAdapter.BestQuestHolder>() {

    inner class BestQuestHolder(val container: FrameLayout) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestQuestHolder {
        val cl = LayoutInflater.from(parent.context).inflate(R.layout.item_best_quest,
                parent, false) as FrameLayout
        return BestQuestHolder(cl)
    }

    override fun getItemCount() = quests.size

    override fun onBindViewHolder(holder: BestQuestHolder, position: Int) {
        val view = holder.container
        val quest = quests[position]
        view.best_quest_name.text = quest.name
        view.best_quest_desc.text = quest.description
        Picasso.get()
            .load(quest.mainPhoto)
            .fit()
            .into(view.best_quest_pic)
    }
}