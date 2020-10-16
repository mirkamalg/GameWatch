package com.mirkamal.gamewatch.ui.fragments.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.mirkamal.gamewatch.R
import kotlinx.android.synthetic.main.fragment_container_intro.*

/**
 * Created by Mirkamal on 15 October 2020
 */
class IntroContainerFragment : Fragment() {

    private val fragments = listOf(IntroFragment(), IntroFragment(), IntroFragment())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_container_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        configureViewPager()
    }

    private fun configureViewPager() {
        viewPagerIntro.adapter = IntroPagerAdapter(this, fragments)
        viewPagerIntro.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                pagerIndicatorViewIntro.selection = position
            }
        })

        pagerIndicatorViewIntro.count = 3
        pagerIndicatorViewIntro.selection = 1
    }

    fun getIndex(fragment: Fragment): Int {
        return fragments.indexOf(fragment)
    }
}