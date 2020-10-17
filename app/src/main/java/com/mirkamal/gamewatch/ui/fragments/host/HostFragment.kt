package com.mirkamal.gamewatch.ui.fragments.host

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.ui.fragments.discover.DiscoverFragment
import com.mirkamal.gamewatch.ui.fragments.mygames.MyGamesFragment
import com.mirkamal.gamewatch.ui.fragments.profile.ProfileFragment
import kotlinx.android.synthetic.main.fragment_host.*

/**
 * Created by Mirkamal on 17 October 2020
 */
class HostFragment : Fragment() {

    private val fragments = listOf(MyGamesFragment(), DiscoverFragment(), ProfileFragment())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        configureStatusBarColor()
        configureViewPager()
        configureBottomNavBar()
    }

    private fun configureStatusBarColor() {
        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> activity?.window?.statusBarColor = Color.parseColor("#00000000")
            Configuration.UI_MODE_NIGHT_UNDEFINED -> activity?.window?.statusBarColor = Color.parseColor("#8a0035")
        }
    }

    private fun configureBottomNavBar() {
        bottomNavigationView.add(MeowBottomNavigation.Model(1, R.drawable.home))
        bottomNavigationView.add(MeowBottomNavigation.Model(2, R.drawable.home))
        bottomNavigationView.add(MeowBottomNavigation.Model(3, R.drawable.home))

        bottomNavigationView.setOnClickMenuListener {
            viewPagerHost.currentItem = it.id - 1
        }

        bottomNavigationView.show(1)
    }

    private fun configureViewPager() {
        viewPagerHost.adapter = HostPagerAdapter(this, fragments)
        viewPagerHost.isUserInputEnabled = false
    }
}