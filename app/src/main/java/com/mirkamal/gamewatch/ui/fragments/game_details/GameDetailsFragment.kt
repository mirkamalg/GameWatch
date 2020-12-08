package com.mirkamal.gamewatch.ui.fragments.game_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.deals.DealsListAdapter
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.screenshots.ScreenshotsListAdapter
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.similar_games.SimilarGamesListAdapter
import com.mirkamal.gamewatch.utils.GAMES_KEY
import com.mirkamal.gamewatch.utils.USER_DATA_COLLECTION_KEY
import com.mirkamal.gamewatch.utils.loadImage
import com.mirkamal.gamewatch.viewmodels.GamesViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.fragment_game_details.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


/**
 * Created by Mirkamal on 28 October 2020
 */
class GameDetailsFragment : Fragment() {

    private val gamesViewModel: GamesViewModel by viewModels()
    private val args: GameDetailsFragmentArgs by navArgs()

    private lateinit var screenshotsAdapter: ScreenshotsListAdapter
    private lateinit var gameDealsAdapter: DealsListAdapter
    private lateinit var similarGamesAdapter: SimilarGamesListAdapter

    private lateinit var skeletonScreenshots: RecyclerViewSkeletonScreen
    private lateinit var skeletonCover: ViewSkeletonScreen

    private var coverUrl = ""

    private lateinit var imageViewerOverlayViewCover: View
    private lateinit var imageViewOverlayViewScreenshots: View
    private lateinit var screenshots: List<ScreenshotPOJO>
    private lateinit var imageViewerScreenshots: StfalconImageViewer<ScreenshotPOJO>
    private lateinit var imageViewerCover: StfalconImageViewer<String>

    private val db = Firebase.firestore
    private val email = Firebase.auth.currentUser?.email ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OverScrollDecoratorHelper.setUpOverScroll(scrollViewGameDetails)

        configureGameData()
        configureRecyclerViews()
        configureShimmerAnimations()
        setOnClickListeners()
        configureObservers()
    }

    override fun onStart() {
        super.onStart()
        loadImages()
        loadDeals()
        loadSimilarGames()
        loadAdditionStatus()
    }

    private fun configureGameData() {
        dropDownTextViewGame.setTitleText(args.game.name)
        dropDownTextViewGame.setContentText(args.game.summary)

        //TODO update to spannable string
        textViewRating.text = "${args.game.rating}/100"
        gamesViewModel.fetchGenre(args.game.genres.joinToString(", "))

    }

    private fun configureShimmerAnimations() {
        skeletonScreenshots = Skeleton.bind(recyclerViewScreenshots)
            .adapter(screenshotsAdapter)
            .load(R.layout.item_skeleton_screenshots)
            .show()
        skeletonCover = Skeleton.bind(imageViewCover)
            .load(R.layout.bg_skeletoon_imageview)
            .show()
    }

    private fun configureObservers() {
        gamesViewModel.screenshots.observe(viewLifecycleOwner, {
            screenshotsAdapter.submitList(it)
            screenshots = it
            configureImageViewers()
            skeletonScreenshots.hide()
        })
        gamesViewModel.coverUrl.observe(viewLifecycleOwner, {
            imageViewCover.loadImage(it)
            coverUrl = it
            skeletonCover.hide()
        })
        gamesViewModel.genreName.observe(viewLifecycleOwner, {
            textViewGenre.text = it
            textViewGenre.isSelected = true //Enable marquee
        })
        gamesViewModel.gameDeals.observe(viewLifecycleOwner, {
            gameDealsAdapter.submitList(it)
            if (!it.isNullOrEmpty()) {
                imageViewPlaceHolderDeals.isVisible = false
            }
        })
        gamesViewModel.similarGames.observe(viewLifecycleOwner, {
            similarGamesAdapter.submitList(it ?: emptyList())
        })
    }

    private fun configureRecyclerViews() {
        screenshotsAdapter = ScreenshotsListAdapter { _, position ->
            imageViewerScreenshots.show()
            imageViewerScreenshots.setCurrentPosition(position)
        }
        recyclerViewScreenshots.adapter = screenshotsAdapter

        gameDealsAdapter = DealsListAdapter()
        recyclerViewDeals.adapter = gameDealsAdapter
        LinearSnapHelper().attachToRecyclerView(recyclerViewScreenshots)

        similarGamesAdapter = SimilarGamesListAdapter()
        recyclerViewSimilarGames.adapter = similarGamesAdapter
    }

    private fun loadImages() {
        if (screenshotsAdapter.currentList.isEmpty()) {
            gamesViewModel.loadScreenShots(args.game.id)
        }

        gamesViewModel.loadCoverPhoto(args.game.id)
    }

    private fun loadDeals() {
        if (gameDealsAdapter.currentList.isEmpty()) {
            gamesViewModel.fetchDeals(args.game.name.split(" ").joinToString("+"))
        }
    }

    private fun loadSimilarGames() {
        if (similarGamesAdapter.currentList.isEmpty()) {
            gamesViewModel.fetchSimilarGames(args.game.similar_games)
        }
    }

    private fun loadAdditionStatus() {
        db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
            if (it.exists()) {
                val gameIDs = it.get(GAMES_KEY) as ArrayList<Long>
                if (gameIDs.contains(args.game.id)) {
                    imageViewAdd.setImageResource(R.drawable.ic_tick)
                }
                imageViewAdd.isVisible = true
                progressBarAdd.isVisible = false
            }
        }
    }

    private fun setOnClickListeners() {
        imageViewGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
        imageViewCover.setOnClickListener {
            imageViewerCover.show()
        }
        imageViewShare.setOnClickListener {
            findNavController().navigate(
                GameDetailsFragmentDirections.actionGameDetailsFragmentToBottomSheetShareGameDetails(
                    args.game
                )
            )
        }
        imageViewAdd.setOnClickListener { view ->
            view.visibility = View.INVISIBLE
            progressBarAdd.isVisible = true
            db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
                if (it.exists()) {
                    val gameIDs = it.get(GAMES_KEY) as ArrayList<Long>
                    if (!gameIDs.contains(args.game.id)) {
                        gameIDs.add(args.game.id)
                        db.collection(USER_DATA_COLLECTION_KEY).document(email).set(
                            hashMapOf(
                                GAMES_KEY to gameIDs
                            ), SetOptions.merge()
                        )
                        imageViewAdd.setImageResource(R.drawable.ic_tick)
                    } else {
                        gameIDs.remove(args.game.id)
                        db.collection(USER_DATA_COLLECTION_KEY).document(email).set(
                            hashMapOf(
                                GAMES_KEY to gameIDs
                            ), SetOptions.merge()
                        )
                        imageViewAdd.setImageResource(R.drawable.ic_add)
                    }
                    view.isVisible = true
                    progressBarAdd.isVisible = false
                }
            }
        }
    }

    private fun configureImageViewers() {
        imageViewerOverlayViewCover = activity?.layoutInflater?.inflate(
            R.layout.overlay_image_viewer,
            imageViewCover.parent as ViewGroup?,
            false
        )!!

        imageViewOverlayViewScreenshots = activity?.layoutInflater?.inflate(
            R.layout.overlay_image_viewer,
            recyclerViewScreenshots.parent as ViewGroup?,
            false
        )!!

        imageViewerCover = StfalconImageViewer.Builder(context, arrayOf(coverUrl)) { view, url ->
            val parsedUrl = url.replace("t_screenshot_med", "t_screenshot_huge")
            Glide.with(requireContext()).load(parsedUrl).into(view)
        }.withTransitionFrom(imageViewCover)
            .withBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blackAlphaDark
                )
            )
            .withOverlayView(imageViewerOverlayViewCover)
            .build()

        imageViewerScreenshots =
            StfalconImageViewer.Builder(context, screenshots.toTypedArray()) { view, pojo ->
                val url = pojo.url?.replace("t_thumb", "t_screenshot_huge")
                Glide.with(requireContext()).load("https:$url").into(view)
            }.withBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blackAlphaDark
                )
            )
                .allowSwipeToDismiss(false)
                .withOverlayView(imageViewOverlayViewScreenshots)
                .build()

        imageViewerOverlayViewCover.findViewById<ImageView>(R.id.imageViewClose)
            .setOnClickListener {
                imageViewerCover.close()
            }
        imageViewOverlayViewScreenshots.findViewById<ImageView>(R.id.imageViewClose)
            .setOnClickListener {
                imageViewerScreenshots.dismiss()
            }
    }
}