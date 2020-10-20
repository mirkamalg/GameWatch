package com.mirkamal.gamewatch.ui.fragments.mygames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.TYPE_PLAYED
import com.mirkamal.gamewatch.utils.TYPE_PLAYING
import com.mirkamal.gamewatch.utils.TYPE_WANT_TO_PLAY
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
        fragments[0].type = TYPE_PLAYING
        fragments[1].type = TYPE_WANT_TO_PLAY
        fragments[2].type = TYPE_PLAYED

        viewPagerMyGames.adapter = MyGamesPagerAdapter(this, fragments)
    }

    private fun configureTabLayout() {
        val tabs = listOf("Playing", "Want to Play", "Played")
        TabLayoutMediator(tabLayoutMyGames, viewPagerMyGames) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}