package com.mirkamal.gamewatch.ui.fragments.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.TYPE_GAMES
import com.mirkamal.gamewatch.utils.TYPE_USERS

/**
 * Created by Mirkamal on 18 October 2020
 */
class InnerDiscoverFragment : Fragment() {

    var type: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inner_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        configureSearchViewBackgroundColor()
        configureFragment()
    }

//    private fun configureSearchViewBackgroundColor() {
//        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
//            Configuration.UI_MODE_NIGHT_YES -> searchViewDiscover.setBackgroundColor(Color.parseColor("#1f1f1f"))
//            Configuration.UI_MODE_NIGHT_NO -> searchViewDiscover.setBackgroundColor(Color.parseColor("#ffffff"))
//        }
//    }

    private fun configureFragment() {
        if (type == TYPE_GAMES) configureFragmentForGames()
        else if (type == TYPE_USERS) configureFragmentForUsers()
    }

    private fun configureFragmentForUsers() {

    }

    private fun configureFragmentForGames() {

    }
}