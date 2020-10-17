package com.mirkamal.gamewatch.ui.fragments.mygames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mirkamal.gamewatch.R
import kotlinx.android.synthetic.main.fragment_my_games.*

/**
 * Created by Mirkamal on 17 October 2020
 */
class MyGamesFragment : Fragment() {

    private val fragments = listOf(
        InnerMyGamesFragment(), InnerMyGamesFragment(), InnerMyGamesFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewPager()
        configureTabLayout()
    }

    private fun configureViewPager() {
        for (fragment in fragments) {
            fragment.type = fragments.indexOf(fragment)
        }
        viewPagerMyGames.adapter = MyGamesPagerAdapter(this, fragments)
    }

    private fun configureTabLayout() {
        val tabs = listOf("Playing", "Want to Play", "Played")
        TabLayoutMediator(tabLayoutMyGames, viewPagerMyGames) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}