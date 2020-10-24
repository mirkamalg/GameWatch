package com.mirkamal.gamewatch.ui.fragments.login

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.Validator
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * Created by Mirkamal on 16 October 2020
 */
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

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
        setTextFont()
        setOnClickListeners()
    }

    private fun configureAuth() {
        auth = Firebase.auth
    }

    private fun setTextFont() {
        val typeface = Typeface.createFromAsset(activity?.assets, "fonts/PT_Sans/PTSans-Italic.ttf")
        textViewRegister.typeface = typeface
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
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun onSuccessfulSignIn() {
        // Create user document in firebase if it doesn't exist
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser?.isEmailVerified!!) {
            Firebase.firestore.collection("userdata").document(currentUser.email ?: "").get()
                .addOnSuccessListener {
                    if (!it.exists()) {
                        Firebase.firestore.collection("userdata").document(currentUser.email ?: "")
                            .set(
                                hashMapOf(
                                    "email" to currentUser.email,
                                    "wanttoplay" to emptyList<Long>(),
                                    "playing" to emptyList<Long>(),
                                    "played" to emptyList<Long>()
                                )
                            )
                    }
                }

            //Navigate to main part
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHostFragment())
        } else {
            Toast.makeText(context, "Please, verify your mail!", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()

//            try {
//                val intent = Intent(Intent.ACTION_MAIN)
//                intent.addCategory(Intent.CATEGORY_APP_EMAIL)
//                activity?.startActivity(intent)
//            } catch (e: Exception) {
//            }
        }


        overlayLayout.isVisible = false
    }

    private fun onFailedSignIn(e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        overlayLayout.isVisible = false
    }

    private fun validateFields(): Boolean {
        return if (Validator.validateEmail(textInputEditTextEmail.text.toString())) {
            if (Validator.validatePassword(textInputEditTextPassword.text.toString())) {
                true
            } else {
                textInputEditTextPassword.error = "Password is invalid"
                false
            }
        } else {
            textInputEditTextEmail.error = "Email is invalid"
            if (!Validator.validatePassword(textInputEditTextPassword.text.toString())) {
                textInputEditTextPassword.error = "Password is invalid"
            }
            false
        }
    }
}