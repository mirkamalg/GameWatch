package com.mirkamal.gamewatch.ui.fragments.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.ByteArrayOutputStream


/**
 * Created by Mirkamal on 16 October 2020
 */
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val storageReference = Firebase.storage.reference

    private val uploadedPicturesCount = MutableLiveData(0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Disable fullscreen after intro
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        configureAuth()
        setOnClickListeners()
        observeValue()
    }

    private fun observeValue() {
        uploadedPicturesCount.observe(viewLifecycleOwner) {
            if (it == 2) {
                //Navigate to main part
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHostFragment())
            }
        }
    }

    private fun configureAuth() {
        auth = Firebase.auth
    }

    private fun setOnClickListeners() {
        buttonLogin.setOnClickListener {
            if (validateFields()) {
                overlayLayout.isVisible = true

                auth.signInWithEmailAndPassword(
                    textInputEditTextEmail.text.toString(),
                    textInputEditTextPassword.text.toString()
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        onSuccessfulSignIn()
                    } else {
                        task.exception?.let { it1 -> onFailedSignIn(it1) }
                    }
                }
            }
        }

        textViewRegister.setOnClickListener {
            val extras = FragmentNavigatorExtras(appLogoContainer to "appLogoContainer")
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment(),
                extras
            )
        }
        textViewForgotPassword.setOnClickListener {
            val extras = FragmentNavigatorExtras(appLogoContainer to "appLogoContainer")
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment(),
                extras
            )
        }
    }

    private fun onSuccessfulSignIn() {
        // Create user document in firebase if it doesn't exist
        val currentUser = FirebaseAuth.getInstance().currentUser

//        if (currentUser?.isEmailVerified!!) {
        Firebase.firestore.collection(USER_DATA_COLLECTION_KEY)
            .document(currentUser?.email ?: "").get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    Firebase.firestore.collection(USER_DATA_COLLECTION_KEY)
                        .document(currentUser?.email ?: "")
                        .set(
                            hashMapOf(
                                EMAIL_KEY to currentUser?.email,
                                GAMES_KEY to emptyList<Long>(),
                                USERNAME_KEY to "Undefinedc",
                                BIO_KEY to ""
                            )
                        )
                }
            }

        // Upload placeholder profile picture
        val profilePictureReference =
            storageReference.child("user_pictures/${currentUser?.email}/profile.png")

        profilePictureReference.downloadUrl.addOnSuccessListener {
            uploadedPicturesCount.value = uploadedPicturesCount.value?.plus(1)
        }.addOnFailureListener {
            val bitmapProfile = BitmapFactory.decodeResource(
                context?.resources,
                R.drawable.profile_picture_placeholder
            )
            val profileStream = ByteArrayOutputStream()
            bitmapProfile.compress(Bitmap.CompressFormat.JPEG, 100, profileStream)
            val profileBytes = profileStream.toByteArray()

            profilePictureReference.putBytes(profileBytes).addOnSuccessListener {
                uploadedPicturesCount.value = uploadedPicturesCount.value?.plus(1)
            }
            Log.e("HERE", it.message.toString(), it)
        }

        //Upload placeholder cover picture
        val coverPictureReference =
            storageReference.child("user_pictures/${currentUser?.email}/cover.png")

        coverPictureReference.downloadUrl.addOnSuccessListener {
            uploadedPicturesCount.value = uploadedPicturesCount.value?.plus(1)
        }.addOnFailureListener {
            val bitmapCover =
                BitmapFactory.decodeResource(
                    context?.resources,
                    R.drawable.cover_picture_placeholder
                )
            val coverStream = ByteArrayOutputStream()
            bitmapCover.compress(Bitmap.CompressFormat.JPEG, 100, coverStream)
            val coverBytes = coverStream.toByteArray()

            coverPictureReference.putBytes(coverBytes).addOnSuccessListener {
                uploadedPicturesCount.value = uploadedPicturesCount.value?.plus(1)
            }
            Log.e("HERE", it.message.toString(), it)
        }

//        } else {
//            Toast.makeText(context, "Please, verify your mail!", Toast.LENGTH_SHORT).show()
//            FirebaseAuth.getInstance().signOut()

//            try {
//                val intent = Intent(Intent.ACTION_MAIN)
//                intent.addCategory(Intent.CATEGORY_APP_EMAIL)
//                activity?.startActivity(intent)
//            } catch (e: Exception) {
//            }
//        }
    }

    private fun onFailedSignIn(e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        overlayLayout.isVisible = false
    }

    private fun validateFields(): Boolean {
        return if (Validator.validateEmail(textInputEditTextEmail.text.toString())) {
            textInputLayoutEmail.error = null

//            if (Validator.validatePassword(textInputEditTextPassword.text.toString())) {
//                textInputLayoutPassword.error = null
//                true
//            } else {
//                textInputLayoutPassword.error = "Password is invalid"
//                false
//            }
            true
        } else {
            textInputLayoutEmail.error = "Email is invalid"
//            if (!Validator.validatePassword(textInputEditTextPassword.text.toString())) {
//                textInputLayoutPassword.error = "Password is invalid"
//            } else {
//                textInputLayoutPassword.error = null
//            }
            false
        }
    }
}