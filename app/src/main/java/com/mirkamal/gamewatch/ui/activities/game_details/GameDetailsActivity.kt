package com.mirkamal.gamewatch.ui.activities.game_details

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
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
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO
import com.mirkamal.gamewatch.ui.activities.game_details.recyclerviews.deals.DealsListAdapter
import com.mirkamal.gamewatch.ui.fragments.dialogs.BottomSheetShareGameDetails
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.screenshots.ScreenshotsListAdapter
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.similar_games.SimilarGamesListAdapter
import com.mirkamal.gamewatch.utils.EXTRA_GAME_KEY
import com.mirkamal.gamewatch.utils.GAMES_KEY
import com.mirkamal.gamewatch.utils.USER_DATA_COLLECTION_KEY
import com.mirkamal.gamewatch.utils.loadImage
import com.mirkamal.gamewatch.viewmodels.GamesViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.fragment_game_details.*

class GameDetailsActivity : AppCompatActivity() {

    private val gamesViewModel: GamesViewModel by lazy {
        ViewModelProvider(this).get(GamesViewModel::class.java)
    }

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

    private lateinit var game: Game

    private lateinit var shareDialog: BottomSheetShareGameDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        configureActivity()

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
        dropDownTextViewGame.setTitleText(game.name)
        dropDownTextViewGame.setContentText(game.summary)

        //TODO update to spannable string
        textViewRating.text = "${game.rating}/100"
        gamesViewModel.fetchGenre(game.genres.joinToString(", "))

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

        similarGamesAdapter = SimilarGamesListAdapter{
            val intent = Intent(this, GameDetailsActivity::class.java)
            intent.putExtra(EXTRA_GAME_KEY, it)
            startActivity(intent)
        }
        recyclerViewSimilarGames.adapter = similarGamesAdapter
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

    private fun setOnClickListeners() {
        imageViewGoBack.setOnClickListener {
            finish()
        }
        imageViewCover.setOnClickListener {
            imageViewerCover.show()
        }
        imageViewShare.setOnClickListener {
            shareDialog.show(supportFragmentManager, "share_dialog")
        }
        imageViewAdd.setOnClickListener { view ->
            view.visibility = View.INVISIBLE
            progressBarAdd.isVisible = true
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
                        imageViewAdd.setImageResource(R.drawable.ic_tick)
                    } else {
                        gameIDs.remove(game.id)
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

    private fun configureObservers() {
        gamesViewModel.screenshots.observe(this, {
            screenshotsAdapter.submitList(it)
            screenshots = it
            configureImageViewers()
            skeletonScreenshots.hide()
        })
        gamesViewModel.coverUrl.observe(this, {
            imageViewCover.loadImage(it)
            coverUrl = it
            skeletonCover.hide()
        })
        gamesViewModel.genreName.observe(this, {
            textViewGenre.text = it
            textViewGenre.isSelected = true //Enable marquee
        })
        gamesViewModel.gameDeals.observe(this, {
            gameDealsAdapter.submitList(it)
            if (!it.isNullOrEmpty()) {
                imageViewPlaceHolderDeals.isVisible = false
            }
        })
        gamesViewModel.similarGames.observe(this, {
            similarGamesAdapter.submitList(it ?: emptyList())
        })
    }

    private fun configureImageViewers() {
        imageViewerOverlayViewCover = layoutInflater.inflate(
            R.layout.overlay_image_viewer,
            imageViewCover.parent as ViewGroup?,
            false
        )!!

        imageViewOverlayViewScreenshots = layoutInflater.inflate(
            R.layout.overlay_image_viewer,
            recyclerViewScreenshots.parent as ViewGroup?,
            false
        )!!

        imageViewerCover = StfalconImageViewer.Builder(this, arrayOf(coverUrl)) { view, url ->
            val parsedUrl = url.replace("t_screenshot_med", "t_screenshot_huge")
            Glide.with(this).load(parsedUrl).into(view)
        }.withTransitionFrom(imageViewCover)
            .withBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.blackAlphaDark
                )
            )
            .withOverlayView(imageViewerOverlayViewCover)
            .build()

        imageViewerScreenshots =
            StfalconImageViewer.Builder(this, screenshots.toTypedArray()) { view, pojo ->
                val url = pojo.url?.replace("t_thumb", "t_screenshot_huge")
                Glide.with(this).load("https:$url").into(view)
            }.withBackgroundColor(
                ContextCompat.getColor(
                    this,
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

    private fun loadImages() {
        if (screenshotsAdapter.currentList.isEmpty()) {
            gamesViewModel.loadScreenShots(game.id)
        }

        gamesViewModel.loadCoverPhoto(game.id)
    }

    private fun loadDeals() {
        if (gameDealsAdapter.currentList.isEmpty()) {
            gamesViewModel.fetchDeals(game.name.split(" ").joinToString("+"))
        }
    }

    private fun loadSimilarGames() {
        if (similarGamesAdapter.currentList.isEmpty()) {
            gamesViewModel.fetchSimilarGames(game.similar_games)
        }
    }

    private fun loadAdditionStatus() {
        db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
            if (it.exists()) {
                val gameIDs = it.get(GAMES_KEY) as ArrayList<Long>
                if (gameIDs.contains(game.id)) {
                    imageViewAdd.setImageResource(R.drawable.ic_tick)
                }
                imageViewAdd.isVisible = true
                progressBarAdd.isVisible = false
            }
        }
    }

    private fun configureActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        game = intent.getParcelableExtra(EXTRA_GAME_KEY)!!

        shareDialog = BottomSheetShareGameDetails()
        shareDialog.injectGame(game)
    }
}