package com.mirkamal.gamewatch.ui.fragments.mygames

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.ui.fragments.mygames.recyclerviews.MyGamesListAdapter
import com.mirkamal.gamewatch.utils.GAMES_KEY
import com.mirkamal.gamewatch.utils.USER_DATA_COLLECTION_KEY
import com.mirkamal.gamewatch.viewmodels.GamesViewModel
import kotlinx.android.synthetic.main.fragment_my_games.*

/**
 * Created by Mirkamal on 17 October 2020
 */
class MyGamesFragment : Fragment() {

    private val db = Firebase.firestore
    private val email = Firebase.auth.currentUser?.email ?: ""
    private val myGamesListAdapter = MyGamesListAdapter()
    private val viewModel: GamesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureFragment()
        configureSwipeRefreshLayout()
    }

    override fun onStart() {
        super.onStart()

        refreshMyGames()
    }

    private fun configureFragment() {
        viewModel.wantToPlayGames.observe(viewLifecycleOwner, {
            myGamesListAdapter.submitList(it)
            myGamesListAdapter.notifyDataSetChanged()
            progressBarMyGames.isVisible = false
            updateVisibility()
        })

        recyclerViewMyGames.adapter = myGamesListAdapter

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
                val game = myGamesListAdapter.currentList[pos].id
                val tempList = arrayListOf<Game>()
                tempList.addAll(myGamesListAdapter.currentList)
                tempList.removeAt(pos)
                myGamesListAdapter.submitList(tempList)
                myGamesListAdapter.notifyDataSetChanged()

                Toast.makeText(context, "Game removed!", Toast.LENGTH_SHORT).show()

                //Add game to "Want to play" array in firebase
                db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
                    if (it.exists()) {
                        val games = it.get(GAMES_KEY) as ArrayList<Long>

                        if (games.contains(game)) {
                            games.remove(game)
                            db.collection(USER_DATA_COLLECTION_KEY).document(email).set(
                                hashMapOf(
                                    GAMES_KEY to games
                                ), SetOptions.merge()
                            )
                        }
                    }

                    updateVisibility()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewMyGames)
    }

    private fun updateVisibility() {
        if (myGamesListAdapter.currentList.isEmpty()) {
            recyclerViewMyGames.isVisible = false
            imageViewDiscover.isVisible = true
            textViewDiscover.isVisible = true
        } else {
            recyclerViewMyGames.isVisible = true
            imageViewDiscover.isVisible = false
            textViewDiscover.isVisible = false
        }
    }

    private fun refreshMyGames() {
        db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
            val ids = it[GAMES_KEY] as List<Long>
            viewModel.onLoadUserGames(ids)

            updateVisibility()
        }
    }

    private fun configureSwipeRefreshLayout() {
        swipeRefreshLayoutMyGames.setOnRefreshListener {
            swipeRefreshLayoutMyGames.isRefreshing = false
            progressBarMyGames.isVisible = true
            refreshMyGames()
        }
    }
}