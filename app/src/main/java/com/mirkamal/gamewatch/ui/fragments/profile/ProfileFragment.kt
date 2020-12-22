package com.mirkamal.gamewatch.ui.fragments.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        uploadDialog = BottomSheetUploadPhoto()
        configureTexts()
        setOnClickListeners()
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
}