package com.mirkamal.gamewatch.ui.fragments.mygames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.ui.fragments.mygames.recyclerviews.MyGamesListAdapter
import com.mirkamal.gamewatch.utils.TYPE_PLAYED
import com.mirkamal.gamewatch.utils.TYPE_PLAYING
import com.mirkamal.gamewatch.utils.TYPE_WANT_TO_PLAY
import com.mirkamal.gamewatch.utils.USER_DATA_COLLECTION
import com.mirkamal.gamewatch.viewmodels.GamesViewModel
import kotlinx.android.synthetic.main.fragment_inner_my_games.*

/**
 * Created by Mirkamal on 18 October 2020
 */
class InnerMyGamesFragment : Fragment() {

    var type: Int = 0
    private val db = Firebase.firestore
    private val email = Firebase.auth.currentUser?.email ?: ""
    private val adapterWantToPlay = MyGamesListAdapter()
    private val adapterPlaying = MyGamesListAdapter()
    private val adapterPlayed = MyGamesListAdapter()
    private val viewModel: GamesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inner_my_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureFragment()
    }

    override fun onStart() {
        super.onStart()

        db.collection(USER_DATA_COLLECTION).document(email).get().addOnSuccessListener {
            val ids = it["wanttoplay"] as List<Long>
            viewModel.onLoadUserGames(ids)
        }
    }

    private fun configureFragment() {
        when (type) {
            TYPE_PLAYED -> configureForPlayedGames()
            TYPE_WANT_TO_PLAY -> configureForWantToPlayGames()
            TYPE_PLAYING -> configureForPlayingGames()
        }
    }

    private fun configureForPlayingGames() {

    }

    private fun configureForWantToPlayGames() {
        viewModel.wantToPlayGames.observe(viewLifecycleOwner, {
            adapterWantToPlay.submitList(it)
            progressBarMyGames.isVisible = false
        })

        recyclerViewMyGames.adapter = adapterWantToPlay
    }

    private fun configureForPlayedGames() {

    }
}