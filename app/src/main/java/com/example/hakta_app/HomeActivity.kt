package com.example.hakta_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hakta_app.models.BestQuestsResponse
import com.example.hakta_app.models.CurrentTaskResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_current_quest.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        App.MAIN_API.getBestQuests(App.TOKEN ?: "")
                .enqueue(object : Callback<BestQuestsResponse> {
                    override fun onFailure(call: Call<BestQuestsResponse>, t: Throwable) {
                        t.message?.let { App.errorAlert(this@HomeActivity, it) }
                    }

                    override fun onResponse(call: Call<BestQuestsResponse>,
                                            response: Response<BestQuestsResponse>) {
                        recycler_best_quests.layoutManager =
                                GridLayoutManager(this@HomeActivity, 2)
                        recycler_best_quests.adapter =
                            response.body()?.content?.let { BestQuestsAdapter(it) }
                    }
                })

        App.MAIN_API.getCurrentTask(App.TOKEN ?: "")
                .enqueue(object : Callback<CurrentTaskResponse> {
                    override fun onFailure(call: Call<CurrentTaskResponse>, t: Throwable) {
                        t.message?.let { App.errorAlert(this@HomeActivity, it) }
                    }

                    override fun onResponse(call: Call<CurrentTaskResponse>,
                                            response: Response<CurrentTaskResponse>) {
                        val currentQuest = response.body()?.quest
                        if (currentQuest != null) {
                            current_quest_name.text = currentQuest.name
                            current_quest_desc.text = currentQuest.description
                            Picasso.get()
                                    .load(currentQuest.mainPhoto)
                                    .fit()
                                    .into(current_quest_pic)
                        }
                        else {
                            card_no_tasks.visibility = View.VISIBLE
                            card_current_quest.visibility = View.GONE
                            card_tasks.visibility = View.GONE
                        }
                    }
                })
    }
}