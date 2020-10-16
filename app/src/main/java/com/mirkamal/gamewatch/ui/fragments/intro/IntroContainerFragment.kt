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

    enum class IntroPageType(val imageResource: Int, val text: String) {
        INTRO_1(R.drawable.drawable_intro_background_1, "Intro 1"),
        INTRO_2(R.drawable.drawable_intro_background_2, "Intro 2"),
        INTRO_3(R.drawable.drawable_intro_background_3, "Intro 3")
    }

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

    fun getType(fragment: Fragment): IntroPageType {
        return when (fragments.indexOf(fragment)) {
            0 -> IntroPageType.INTRO_1
            1 -> IntroPageType.INTRO_2
            2 -> IntroPageType.INTRO_3
            else -> IntroPageType.INTRO_1
        }
    }
}