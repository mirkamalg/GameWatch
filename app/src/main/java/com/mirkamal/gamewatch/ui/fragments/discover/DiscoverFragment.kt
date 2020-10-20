package com.mirkamal.gamewatch.ui.fragments.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.TYPE_GAMES
import com.mirkamal.gamewatch.utils.TYPE_USERS
import kotlinx.android.synthetic.main.fragment_discover.*

/**
 * Created by Mirkamal on 17 October 2020
 */
class DiscoverFragment : Fragment() {

    private val fragments = listOf(InnerDiscoverFragment(), InnerDiscoverFragment())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewPager()
        configureTabLayout()
    }

    private fun configureTabLayout() {

        val tabs = listOf("Games", "Users")
        TabLayoutMediator(tabLayoutDiscover, viewPagerDiscover) { tab, position ->
            tab.text = tabs[position]
        }.attach()

    }

    private fun configureViewPager() {
        fragments[0].type = TYPE_GAMES
        fragments[1].type = TYPE_USERS

        viewPagerDiscover.adapter = DiscoverPagerAdapter(this, fragments)
    }
}