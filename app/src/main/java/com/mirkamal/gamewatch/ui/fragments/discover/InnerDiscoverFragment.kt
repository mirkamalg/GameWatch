package com.mirkamal.gamewatch.ui.fragments.discover

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.parcel.Game
import com.mirkamal.gamewatch.ui.activities.game_details.GameDetailsActivity
import com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.adapters.DiscoverGamesListAdapter
import com.mirkamal.gamewatch.utils.*
import com.mirkamal.gamewatch.viewmodels.GamesViewModel
import kotlinx.android.synthetic.main.fragment_inner_discover.*


/**
 * Created by Mirkamal on 18 October 2020
 */
class InnerDiscoverFragment : Fragment() {

    var type: Int = 3
    private lateinit var gamesViewModel: GamesViewModel
    private lateinit var discoverGamesListAdapter: DiscoverGamesListAdapter

    private val db = Firebase.firestore
    private val email = Firebase.auth.currentUser?.email ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inner_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureSearchViewBackgroundColor()
        configureFragment()
    }

    private fun configureSearchViewBackgroundColor() {
        if (context?.isDarkThemeOn() == true) {
            searchViewDiscover.setBackgroundColor(Color.parseColor("#1f1f1f"))
        } else {
            searchViewDiscover.setBackgroundColor(Color.parseColor("#ffffff"))
        }
    }

    private fun configureFragment() {
        if (type == TYPE_GAMES) configureFragmentForGames()
        else if (type == TYPE_USERS) configureFragmentForUsers()
    }

    private fun configureFragmentForUsers() {
        searchViewDiscover.isVisible = false
        animationViewDiscoverGames.setAnimation("coming_soon_anim.json")
        animationViewDiscoverGames.repeatCount = -1

        textViewDiscoverGamesLabel.text = getString(R.string.msg_not_available_yet)

        Handler(Looper.getMainLooper()).postDelayed({
            animationViewDiscoverGames.playAnimation()
        }, 200)
    }

    private fun configureFragmentForGames() {
        val viewmodel: GamesViewModel by viewModels()
        gamesViewModel = viewmodel

        configureRecyclerViewForGames()
        configureSearch()
        configureObservers()
    }

    private fun configureRecyclerViewForGames() {
        discoverGamesListAdapter = DiscoverGamesListAdapter {
            val intent = Intent(context, GameDetailsActivity::class.java)
            intent.putExtra(EXTRA_GAME_KEY, it)
            startActivity(intent)
        }
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
                val game = discoverGamesListAdapter.currentList[pos]
                val tempList = arrayListOf<Game>()
                tempList.addAll(discoverGamesListAdapter.currentList)
                tempList.removeAt(pos)
                discoverGamesListAdapter.submitList(tempList)
                discoverGamesListAdapter.notifyDataSetChanged()

                Toast.makeText(context, "Game added!", Toast.LENGTH_SHORT).show()

                //Add game to "games" array in firebase
                db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
                    if (it.exists()) {
                        val gameIDs = it.get(GAMES_KEY) as ArrayList<Long>
                        if (!gameIDs.contains(game.id)) {
                            gameIDs.add(game.id)
                            db.collection(USER_DATA_COLLECTION_KEY).document(email).set(
                                hashMapOf(
                                    GAMES_KEY to gameIDs
                                ), SetOptions.merge()
                            )
                        }
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewDiscover)
    }

    private fun configureObservers() {
        gamesViewModel.resultGames.observe(viewLifecycleOwner, {
            discoverGamesListAdapter.submitList(it)
            hideLoadingAnimation()

            if (it.isNotEmpty()) {
                animationViewDiscoverGames.isVisible = false
                textViewDiscoverGamesLabel.isVisible = false
            } else {
                animationViewDiscoverGames.isVisible = true
                textViewDiscoverGamesLabel.isVisible = true

                textViewDiscoverGamesLabel.text = getString(R.string.msg_discover_nothing_found)

                animationViewDiscoverGames.setAnimation("empty_box_anim.json")
                animationViewDiscoverGames.repeatCount = 0
                animationViewDiscoverGames.repeatMode = LottieDrawable.RESTART
                Handler(Looper.getMainLooper()).postDelayed({
                    animationViewDiscoverGames.playAnimation()
                }, 200)
            }
        })
    }

    private fun configureSearch() {

        searchViewDiscover.setOnSearchListener(object : FloatingSearchView.OnSearchListener {

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {}

            override fun onSearchAction(currentQuery: String?) {
                gamesViewModel.onSearch(currentQuery ?: "")

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