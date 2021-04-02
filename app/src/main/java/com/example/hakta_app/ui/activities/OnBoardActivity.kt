package com.example.hakta_app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.hakta_app.PrefManager
import com.example.hakta_app.R
import com.example.hakta_app.ui.fragments.FirstSlideFragment
import com.example.hakta_app.ui.fragments.SecondSlideFragment
import com.example.hakta_app.ui.fragments.ThirdSlideFragment
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {

    val slides = arrayOf(
        R.layout.on_board_slide1,
        R.layout.on_board_slide2,
        R.layout.on_board_slide3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        val dots = onboard_dots.children
        dots.elementAt(0).background = getDrawable(R.drawable.onboard_selected_dot)

        val pagerListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == slides.size - 1) {
                    btn_next.visibility = View.INVISIBLE
                    btn_skip.visibility = View.INVISIBLE
                    btn_start.visibility = View.VISIBLE
                }
                else {
                    btn_next.visibility = View.VISIBLE
                    btn_skip.visibility = View.VISIBLE
                    btn_start.visibility = View.INVISIBLE
                }
                dots.forEach { it.background = getDrawable(R.drawable.onboard_dot) }
                dots.elementAt(position).background = getDrawable(R.drawable.onboard_selected_dot)
            }
        }
        onboard_pager.adapter = OnBoardPagerAdapter(this)
        onboard_pager.registerOnPageChangeCallback(pagerListener)

        btn_next.setOnClickListener {
            onboard_pager.currentItem  = onboard_pager.currentItem + 1
        }
        btn_start.setOnClickListener { toSignInActivity() }
        btn_skip.setOnClickListener { toSignInActivity() }


    }

    private fun toSignInActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
    }


    inner class OnBoardPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = slides.size

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FirstSlideFragment()
                1 -> SecondSlideFragment()
                2 -> ThirdSlideFragment()
                else -> FirstSlideFragment()
            }
        }
    }
}