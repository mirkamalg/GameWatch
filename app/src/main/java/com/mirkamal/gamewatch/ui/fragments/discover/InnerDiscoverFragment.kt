package com.mirkamal.gamewatch.ui.fragments.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.adapters.DiscoverGamesListAdapter
import com.mirkamal.gamewatch.utils.TYPE_GAMES
import com.mirkamal.gamewatch.utils.TYPE_USERS
import com.mirkamal.gamewatch.viewmodels.DiscoverGamesViewModel
import kotlinx.android.synthetic.main.fragment_inner_discover.*


/**
 * Created by Mirkamal on 18 October 2020
 */
class InnerDiscoverFragment : Fragment() {

    var type: Int = 3
    private lateinit var discoverGamesViewModel: DiscoverGamesViewModel
    private lateinit var discoverGamesListAdapter: DiscoverGamesListAdapter

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
        val viewmodel: DiscoverGamesViewModel by viewModels()
        discoverGamesViewModel = viewmodel

        configureRecyclerViewForGames()
        configureSearch()
        configureObservers()
    }

    private fun configureRecyclerViewForGames() {
        discoverGamesListAdapter = DiscoverGamesListAdapter()
        recyclerViewDiscover.adapter = discoverGamesListAdapter

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val pos = viewHolder.adapterPosition
                val tempList = arrayListOf<Game>()
                tempList.addAll(discoverGamesListAdapter.currentList)
                tempList.removeAt(pos)
                discoverGamesListAdapter.submitList(tempList)
                discoverGamesListAdapter.notifyDataSetChanged()

                Toast.makeText(context, "Game added!", Toast.LENGTH_SHORT).show()

                //TODO implement game addition
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewDiscover)
    }

    private fun configureObservers() {
        discoverGamesViewModel.resultGames.observe(viewLifecycleOwner, {
            discoverGamesListAdapter.submitList(it)
            hideLoadingAnimation()
        })
    }

    private fun configureSearch() {

        searchViewDiscover.setOnSearchListener(object : FloatingSearchView.OnSearchListener {

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {}

            override fun onSearchAction(currentQuery: String?) {
                discoverGamesViewModel.onSearch(currentQuery ?: "")

                showLoadingAnimation()
            }

        })
    }

    private fun showLoadingAnimation() {
        progressBarDiscoverGames.isVisible = true
    }

    private fun hideLoadingAnimation() {
        progressBarDiscoverGames.isVisible = false
    }
}