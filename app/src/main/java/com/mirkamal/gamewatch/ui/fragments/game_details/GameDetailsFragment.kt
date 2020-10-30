package com.mirkamal.gamewatch.ui.fragments.game_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.entity.GameEntity
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.screenshots.ScreenshotsListAdapter
import com.mirkamal.gamewatch.viewmodels.GamesViewModel
import kotlinx.android.synthetic.main.fragment_game_details.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

/**
 * Created by Mirkamal on 28 October 2020
 */
class GameDetailsFragment : Fragment() {

    private val gamesViewModel: GamesViewModel by viewModels()
    val args: GameDetailsFragmentArgs by navArgs()
    private lateinit var gameEntity: GameEntity

    private lateinit var screenshotsAdapter: ScreenshotsListAdapter

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameEntity = gamesViewModel.fetchGameFromLocalDatabase(args.gameID)

        OverScrollDecoratorHelper.setUpOverScroll(scrollViewGameDetails)
        configureRecyclerViews()
        setOnClickListeners()
        configureObservers()
    }

    override fun onStart() {
        super.onStart()
        loadScreenshots()
    }

    private fun configureObservers() {
        gamesViewModel.screenshots.observe(viewLifecycleOwner, {
            screenshotsAdapter.submitList(it)
            skeletonScreen.hide()
        })
    }

    private fun configureRecyclerViews() {
        screenshotsAdapter = ScreenshotsListAdapter()
        recyclerViewScreenshots.adapter = screenshotsAdapter
        skeletonScreen = Skeleton.bind(recyclerViewScreenshots)
            .adapter(screenshotsAdapter)
            .load(R.layout.item_skeleton_screenshots)
            .show()
    }

    private fun loadScreenshots() {
        if (screenshotsAdapter.currentList.isEmpty()) {
            gamesViewModel.loadScreenShots(args.gameID)
        }
    }

    private fun setOnClickListeners() {
        imageViewGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}