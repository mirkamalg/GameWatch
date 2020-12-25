package com.mirkamal.gamewatch.ui.fragments.dialogs

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mirkamal.gamewatch.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.dialog_upload_photo.*


/**
 * Created by Mirkamal on 22 December 2020
 */
class BottomSheetUploadPhoto(val onCancel: () -> Unit) : BottomSheetDialogFragment() {

    private val storageReference = Firebase.storage.reference
    private val email = Firebase.auth.currentUser?.email ?: ""
    private lateinit var type: UPLOAD

    private enum class UPLOAD {
        PROFILE, COVER
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_upload_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewContainerProfile.setOnClickListener {
            type = UPLOAD.PROFILE
            context?.let { context ->
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(context, this)
            }
        }

        cardViewContainerCover.setOnClickListener {
            type = UPLOAD.COVER
            context?.let { context ->
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(context, this)
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        onCancel()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        onCancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                progressBar.isVisible = true
                val resultUri: Uri = result.uri
                when (type) {
                    UPLOAD.PROFILE -> {
                        val profilePictureReference =
                            storageReference.child("user_pictures/$email/profile.png")

                        val uploadTask = profilePictureReference.putFile(resultUri)
                        uploadTask.addOnSuccessListener {
                            Toast.makeText(context, "Profile picture updated!", Toast.LENGTH_SHORT)
                                .show()
                            dismiss()
                        }.addOnFailureListener {

                        }
                    }
                    UPLOAD.COVER -> {
                        val profilePictureReference =
                            storageReference.child("user_pictures/$email/cover.png")

                        val uploadTask = profilePictureReference.putFile(resultUri)
                        uploadTask.addOnSuccessListener {
                            Toast.makeText(context, "Cover updated!", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }.addOnFailureListener {
                            Log.e("UPLOAD_PROFILE_PICTURE", it.message ?: "", it)
                        }
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("IMAGE_CROPPER", result.error.message ?: "", result.error)
            }
        }
    }
}