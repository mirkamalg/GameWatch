package com.mirkamal.gamewatch.ui.fragments.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.parcel.ProfileData
import com.mirkamal.gamewatch.ui.activities.edit_profile.EditProfileActivity
import com.mirkamal.gamewatch.ui.fragments.dialogs.BottomSheetUploadPhoto
import com.mirkamal.gamewatch.ui.fragments.dialogs.DialogLogOutConfirmation
import com.mirkamal.gamewatch.utils.*
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * Created by Mirkamal on 17 October 2020
 */
class ProfileFragment : Fragment() {

    private val db = Firebase.firestore
    private val email = Firebase.auth.currentUser?.email ?: ""

    private lateinit var uploadDialog: BottomSheetUploadPhoto
    private lateinit var logOutDialog: DialogLogOutConfirmation

    private lateinit var profilePictureDownloadURL: String

    private val accounts: ArrayList<Map<String, String>> = arrayListOf()

    private val completedDownloadCount = MutableLiveData(0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTexts()
        setOnClickListeners()
        configureDialogs()
        configureObservers()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            configureScrollView()
        }
    }

    override fun onStart() {
        super.onStart()

        fetchUserData()
    }

    private fun configureObservers() {
        completedDownloadCount.observe(viewLifecycleOwner) {
            if (it == 2) {
                overlayLayout.isVisible = false
                progressBar.isVisible = false
            }
        }
    }

    private fun setOnClickListeners() {
        profilePictureContainer.setOnClickListener {
            uploadDialog.show(childFragmentManager, "upload_dialog")
        }

        imageViewProfileCover.setOnClickListener {
            uploadDialog.show(childFragmentManager, "upload_dialog")
        }
        imageViewGithub.setOnClickListener {
            openURL("https://github.com/Re1r0/GameWatch")
        }
        imageViewEditProfile.setOnClickListener {
            if (this::profilePictureDownloadURL.isInitialized) {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                val profileData =
                    ProfileData(
                        textViewUsername.text.toString(),
                        textViewBio.text.toString(),
                        profilePictureDownloadURL,
                        accounts[0],
                        accounts[1],
                        accounts[2],
                        accounts[3]
                    )
                intent.putExtra(EXTRA_PROFILE_DATA_KEY, profileData)
                startActivity(intent)
            }
        }
        buttonLogOut.setOnClickListener {
            logOutDialog.show(childFragmentManager, "log_out_dialog")
        }
    }

    private fun fetchUserData() {
        db.collection(USER_DATA_COLLECTION_KEY).document(email).get()
            .addOnSuccessListener { documentSnapshot ->
                textViewUsername.text = documentSnapshot[USERNAME_KEY] as String
                textViewBio.text = documentSnapshot[BIO_KEY] as String
                textViewGameCount.text = getString(
                    R.string.msg_game_count_profile,
                    (documentSnapshot[GAMES_KEY] as List<*>).size
                )

                // Handle accounts
                val steam = documentSnapshot[STEAM_KEY] as Map<String, String>?
                accounts.add(steam ?: emptyMap())

                val displayNameSteam = steam?.get(DISPLAY_NAME_KEY) ?: ""

                if (displayNameSteam.isNotBlank()) {
                    textViewDisplayNameSteam.text = displayNameSteam
                    buttonCopySteamURL.setOnClickListener {
                        copyToClipboard(steam?.get(URL_KEY) ?: "")
                    }
                    buttonOpenSteamURL.setOnClickListener {
                        openURL(steam?.get(URL_KEY) ?: "https://store.steampowered.com/")
                    }
                } else {
                    cardViewSteamProfile.isVisible = false
                }

                val epicGames = documentSnapshot[EPIC_GAMES_KEY] as Map<String, String>?
                accounts.add(epicGames ?: emptyMap())

                val displayNameEpicGames = epicGames?.get(DISPLAY_NAME_KEY) ?: ""


                if (displayNameEpicGames.isNotBlank()) {
                    textViewDisplayNameEpicGames.text = displayNameEpicGames
                    buttonCopyEpicGamesEmail.setOnClickListener {
                        copyToClipboard(epicGames?.get(URL_KEY) ?: "")
                    }
                } else {
                    cardViewEpicGamesProfile.isVisible = false
                }

                val uplay = documentSnapshot[UPLAY_KEY] as Map<String, String>?
                accounts.add(uplay ?: emptyMap())

                val displayNameUplay = uplay?.get(DISPLAY_NAME_KEY) ?: ""

                if (displayNameUplay.isNotBlank()) {
                    textViewDisplayNameUplay.text = displayNameUplay
                    buttonCopyUplayURL.setOnClickListener {
                        copyToClipboard(uplay?.get(URL_KEY) ?: "")
                    }
                    buttonOpenUplayURL.setOnClickListener {
                        openURL(uplay?.get(URL_KEY) ?: "https://ubisoftconnect.com/en-US/")
                    }
                } else {
                    cardViewUplayProfile.isVisible = false
                }

                val discord = documentSnapshot[DISCORD_KEY] as Map<String, String>?
                accounts.add(discord ?: emptyMap())

                val displayNameDiscord = discord?.get(DISPLAY_NAME_KEY) ?: ""
                val discordTag = discord?.get(URL_KEY) ?: ""

                if (displayNameDiscord.isNotBlank()) {
                    textViewDisplayNameDiscord.text = displayNameDiscord
                    buttonCopyDiscordTag.setOnClickListener {
                        copyToClipboard("$displayNameDiscord$discordTag")
                    }
                } else {
                    cardViewDiscordProfile.isVisible = false
                }

            }

        val threeMegabytes = (1024 * 1024 * 3).toLong()
        val profilePictureReference =
            Firebase.storage.reference.child("user_pictures/$email/profile.png")
        profilePictureReference.getBytes(threeMegabytes).addOnSuccessListener { bytes ->
            imageViewProfilePicture.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.size
                )
            )

            profilePictureReference.downloadUrl.addOnSuccessListener {
                profilePictureDownloadURL = it.toString()
            }

            completedDownloadCount.value = completedDownloadCount.value?.plus(1)
        }

        val coverImageReference = Firebase.storage.reference.child("user_pictures/$email/cover.png")
        coverImageReference.getBytes(threeMegabytes).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Blurry.with(context).animate(1000).async().from(bitmap).into(imageViewProfileCover)
            viewGradient.isVisible = true

            completedDownloadCount.value = completedDownloadCount.value?.plus(1)
        }
    }

    private fun configureTexts() {
        textViewEmail.text = email
    }

    private fun configureDialogs() {
        uploadDialog = BottomSheetUploadPhoto {
            fetchUserData()
        }
        logOutDialog = DialogLogOutConfirmation()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureScrollView() {
        scrollViewProfile.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            //Animation on scroll is handled here
            val parameters = profilePictureContainer.layoutParams as ConstraintLayout.LayoutParams

            val oldBias = parameters.horizontalBias
            val newBias: Float

            val oldWidthPercent = parameters.matchConstraintPercentWidth
            val newWidthPercent: Float

            if (oldScrollY < scrollY) {
                newBias = 0.5f - (0.002f * scrollY)
                newWidthPercent = 0.3f - (0.00075f * scrollY)
            } else {
                newBias = oldBias + (0.002f * (oldScrollY - scrollY))
                newWidthPercent = oldWidthPercent + (0.00075f * (oldScrollY - scrollY))
            }

            if (newBias in 0.0..0.5) {
                parameters.horizontalBias = newBias
                parameters.matchConstraintPercentWidth = newWidthPercent
            }

            profilePictureContainer.layoutParams = parameters
        }
    }
}