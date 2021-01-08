package com.mirkamal.gamewatch.ui.activities.edit_profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.parcel.ProfileData
import com.mirkamal.gamewatch.utils.*
import com.mirkamal.gamewatch.viewmodels.ProfileViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileData: ProfileData

    private val storageReference = Firebase.storage.reference
    private val email = Firebase.auth.currentUser?.email ?: ""

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        setOnClickListeners()
        handleExtraData()
        configureEditTextValidation()
        configureObservers()
    }

    override fun onStart() {
        super.onStart()

        imageViewProfilePicture.loadImage(profileData.profilePictureURL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                val profilePictureReference =
                    storageReference.child("user_pictures/$email/profile.png")

                progressBar.isVisible = true
                overlayLayout.isVisible = true

                val uploadTask = profilePictureReference.putFile(resultUri)
                uploadTask.addOnSuccessListener {
                    Toast.makeText(applicationContext, "Profile picture updated!", Toast.LENGTH_SHORT)
                        .show()

                    profilePictureReference.downloadUrl.addOnSuccessListener {
                        imageViewProfilePicture.loadImage(it.toString())
                        progressBar.isVisible = false
                        overlayLayout.isVisible = false
                    }.addOnFailureListener {
                        progressBar.isVisible = false
                        overlayLayout.isVisible = false
                    }
                }.addOnFailureListener {
                    Log.e("EditProfileActivity", "Update profile pic failed", it)

                    progressBar.isVisible = false
                    overlayLayout.isVisible = false
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("IMAGE_CROPPER", result.error.message ?: "", result.error)
            }
        }
    }

    private fun configureEditTextValidation() {
        //Display names
        textInputEditTextUsername.doAfterTextChanged {
            textInputLayoutUsername.error = null
            profileViewModel.validateUsername(it.toString())
        }
        textInputEditTextSteamDisplayName.doAfterTextChanged {
            textInputLayoutSteamDisplayName.error = null
//            profileViewModel.validateDisplayName(it.toString(), ACCOUNT_STEAM)
        }
        textInputEditTextEpicGamesDisplayName.doAfterTextChanged {
            textInputLayoutEpicGamesDisplayName.error = null
//            profileViewModel.validateDisplayName(it.toString(), ACCOUNT_EPIC_GAMES)
        }
        textInputEditTextUplayDisplayName.doAfterTextChanged {
            textInputLayoutUplayDisplayName.error = null
//            profileViewModel.validateDisplayName(it.toString(), ACCOUNT_UPLAY)
        }
        textInputEditTextDiscordDisplayName.doAfterTextChanged {
            textInputLayoutDiscordDisplayName.error = null
//            profileViewModel.validateDisplayName(it.toString(), ACCOUNT_DISCORD)
        }


        //Others
        textInputEditTextSteamURL.doAfterTextChanged {
            profileViewModel.validateSteamURL(it.toString())
        }
        textInputEditTextEpicGamesEmail.doAfterTextChanged {
            profileViewModel.validateEmail(it.toString())
        }
        textInputEditTextUplayURL.doAfterTextChanged {
            profileViewModel.validateUplayURL(it.toString())
        }
        textInputEditTextDiscordTag.doAfterTextChanged {
            profileViewModel.validateDiscordTag(it.toString())
        }
    }

    private fun configureObservers() {
        profileViewModel.userNameValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutUsername.error = it.message
            }
        }
        profileViewModel.steamDisplayNameValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutSteamDisplayName.error = it.message
            }
        }
        profileViewModel.epicGamesDisplayNameValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutEpicGamesDisplayName.error = it.message
            }
        }
        profileViewModel.uplayDisplayNameValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutUplayDisplayName.error = it.message
            }
        }
        profileViewModel.discordDisplayNameValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutDiscordDisplayName.error = it.message
            }
        }
        profileViewModel.steamURLValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutSteamURL.error = it.message
            } else {
                textInputLayoutSteamURL.error = null
            }
        }
        profileViewModel.epicGamesEmailValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutEpicGamesEmail.error = it.message
            } else {
                textInputLayoutEpicGamesEmail.error = null
            }
        }
        profileViewModel.uplayURLValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutUplayURL.error = it.message
            } else {
                textInputLayoutUplayURL.error = null
            }
        }
        profileViewModel.discordTagValidationResult.observe(this) {
            if (!it.isValid) {
                textInputLayoutDiscordTag.error = it.message
            } else {
                textInputLayoutDiscordTag.error = null
            }
        }
        profileViewModel.updateProfileResult.observe(this) {
            when (it) {
                SUCCESS -> {
                    Snackbar.make(
                        buttonSave,
                        "Profile successfully updated.",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
                FAILURE -> {
                    Snackbar.make(
                        buttonSave,
                        "Failed to update profile. Try again.",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }

            overlayLayout.isVisible = false
            progressBar.isVisible = false
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun handleExtraData() {
        profileData = intent.getParcelableExtra(EXTRA_PROFILE_DATA_KEY)!!

        textInputEditTextUsername.setText(profileData.username)
        textInputEditTextBio.setText(profileData.bio)

        textInputEditTextSteamDisplayName.setText(profileData.steam[DISPLAY_NAME_KEY] ?: "")
        textInputEditTextSteamURL.setText(profileData.steam[URL_KEY] ?: "")

        textInputEditTextEpicGamesDisplayName.setText(profileData.epicGames[DISPLAY_NAME_KEY] ?: "")
        textInputEditTextEpicGamesEmail.setText(profileData.epicGames[URL_KEY] ?: "")

        textInputEditTextUplayDisplayName.setText(profileData.uplay[DISPLAY_NAME_KEY] ?: "")
        textInputEditTextUplayURL.setText(profileData.uplay[URL_KEY] ?: "")

        textInputEditTextDiscordDisplayName.setText(profileData.discord[DISPLAY_NAME_KEY] ?: "")
        textInputEditTextDiscordTag.setText(profileData.discord[URL_KEY] ?: "")
    }

    private fun setOnClickListeners() {
        imageViewGoBack.setOnClickListener {
            finish()
        }
        cardViewSteam.setOnClickListener {
            updateCardVisibilities(STEAM_KEY)
        }
        cardViewEpicGames.setOnClickListener {
            updateCardVisibilities(EPIC_GAMES_KEY)
        }
        cardViewUplay.setOnClickListener {
            updateCardVisibilities(UPLAY_KEY)
        }
        cardViewDiscord.setOnClickListener {
            updateCardVisibilities(DISCORD_KEY)
        }
        buttonSave.setOnClickListener {
            if (isAllFieldsValid()) {
                overlayLayout.isVisible = true
                progressBar.isVisible = true
                window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

                profileViewModel.updateUserInfo(
                    textInputEditTextUsername.text.toString(),
                    textInputEditTextBio.text.toString().trim(),
                    textInputEditTextSteamDisplayName.text.toString(),
                    textInputEditTextSteamURL.text.toString(),
                    textInputEditTextEpicGamesDisplayName.text.toString(),
                    textInputEditTextEpicGamesEmail.text.toString(),
                    textInputEditTextUplayDisplayName.text.toString(),
                    textInputEditTextUplayURL.text.toString(),
                    textInputEditTextDiscordDisplayName.text.toString(),
                    textInputEditTextDiscordTag.text.toString()
                )
            } else {
                Snackbar.make(buttonSave, "Make sure all fields are valid.", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        profilePictureContainer.setOnClickListener {
            applicationContext.let { context ->
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this)
            }
        }
    }

    private fun updateCardVisibilities(account: String) {
        val layouts = listOf(
            linearLayoutSteamEdit,
            linearLayoutEpicGamesEdit,
            linearLayoutUplayEdit,
            linearLayoutDiscordEdit
        )
        when (account) {
            STEAM_KEY -> {
                layouts[0].isVisible = !layouts[0].isVisible
                for (layout in layouts.subList(1, 3)) {
                    layout.isVisible = false
                }
            }
            EPIC_GAMES_KEY -> {
                layouts[1].isVisible = !layouts[1].isVisible
                layouts[0].isVisible = false
                layouts[2].isVisible = false
                layouts[3].isVisible = false
            }
            UPLAY_KEY -> {
                layouts[2].isVisible = !layouts[2].isVisible
                layouts[0].isVisible = false
                layouts[1].isVisible = false
                layouts[3].isVisible = false
            }
            DISCORD_KEY -> {
                layouts[3].isVisible = !layouts[3].isVisible
                layouts[0].isVisible = false
                layouts[1].isVisible = false
                layouts[2].isVisible = false
            }
        }
    }

    private fun isAllFieldsValid(): Boolean {
        return textInputLayoutUsername.error.isNullOrEmpty() &&
                textInputLayoutBio.error.isNullOrEmpty() &&
                textInputLayoutSteamDisplayName.error.isNullOrEmpty() &&
                textInputLayoutSteamURL.error.isNullOrEmpty() &&
                textInputLayoutEpicGamesDisplayName.error.isNullOrEmpty() &&
                textInputLayoutSteamURL.error.isNullOrEmpty() &&
                textInputLayoutUplayDisplayName.error.isNullOrEmpty() &&
                textInputLayoutUplayURL.error.isNullOrEmpty() &&
                textInputLayoutDiscordDisplayName.error.isNullOrEmpty() &&
                textInputLayoutDiscordTag.error.isNullOrEmpty()
    }
}