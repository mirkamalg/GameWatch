package com.mirkamal.gamewatch.ui.fragments.profile

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.ui.fragments.dialogs.BottomSheetUploadPhoto
import com.mirkamal.gamewatch.utils.BIO_KEY
import com.mirkamal.gamewatch.utils.USERNAME_KEY
import com.mirkamal.gamewatch.utils.USER_DATA_COLLECTION_KEY
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * Created by Mirkamal on 17 October 2020
 */
class ProfileFragment : Fragment() {

    private val db = Firebase.firestore
    private val email = Firebase.auth.currentUser?.email ?: ""

    private lateinit var uploadDialog: BottomSheetUploadPhoto

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
        configureUploadDialog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            configureScrollView()
        }
    }

    override fun onStart() {
        super.onStart()

        fetchUserData()
    }

    private fun setOnClickListeners() {
        profilePictureContainer.setOnClickListener {
            uploadDialog.show(childFragmentManager, "upload_dialog")
        }

        imageViewProfileCover.setOnClickListener {
            uploadDialog.show(childFragmentManager, "upload_dialog")
        }
    }

    private fun fetchUserData() {
        db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
            textViewUsername.text = it[USERNAME_KEY] as String
            textViewBio.text = it[BIO_KEY] as String
        }

        val threeMegabytes = (1024 * 1024 * 3).toLong()
        val profilePictureReference =
            Firebase.storage.reference.child("user_pictures/$email/profile.png")
        profilePictureReference.getBytes(threeMegabytes).addOnSuccessListener {
            imageViewProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }

        val coverImageReference = Firebase.storage.reference.child("user_pictures/$email/cover.png")
        coverImageReference.getBytes(threeMegabytes).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Blurry.with(context).animate(1000).async().from(bitmap).into(imageViewProfileCover)
            viewGradient.isVisible = true
        }

    }

    private fun configureTexts() {
        textViewEmail.text = email
    }

    private fun configureUploadDialog() {
        uploadDialog = BottomSheetUploadPhoto {
            fetchUserData()
        }
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