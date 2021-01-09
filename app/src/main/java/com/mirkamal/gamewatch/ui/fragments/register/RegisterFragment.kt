package com.mirkamal.gamewatch.ui.fragments.register

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.Validator
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * Created by Mirkamal on 17 October 2020
 */
class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        buttonSignUp.setOnClickListener {
            if (validateFields()) {
                overlayLayout.isVisible = true

                auth.createUserWithEmailAndPassword(
                    textInputEditTextEmail.text.toString(),
                    textInputEditTextPassword.text.toString()
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        onSuccessfulRegister()
                    } else {
                        task.exception?.let { it1 -> onFailedRegister(it1) }
                    }
                }
            }
        }
        buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onSuccessfulRegister() {
        Toast.makeText(
            context,
            "Successfully registered, please check your mail for confirmation",
            Toast.LENGTH_SHORT
        ).show()

        //Send verification mail and sign out (verification is required in order to log in)
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
        FirebaseAuth.getInstance().signOut()

        findNavController().popBackStack()
    }

    private fun onFailedRegister(e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        overlayLayout.isVisible = false
    }

    private fun validateFields(): Boolean {
        var flag: Boolean
        val email = textInputEditTextEmail.text.toString()
        val password = textInputEditTextPassword.text.toString()
        val rPassword = textInputEditTextRepeatPassword.text.toString()

        if (email.isEmpty()) {
            textInputLayoutEmail.error = "Field is empty!"
        } else {
            flag = Validator.validateEmail(email)
            if (!flag) {
                textInputLayoutEmail.error = "Email is invalid!"
            } else {
                textInputLayoutEmail.error = null
            }
        }

        if (password.isEmpty()) {
            flag = false
            textInputLayoutPassword.error = "Field is empty!"
        } else {
            flag = Validator.validatePassword(password)
            if (!flag) {
                textInputLayoutPassword.error = "Password is invalid!"
            } else {
                textInputLayoutPassword.error = null
                if (password != rPassword) {
                    flag = false
                    textInputLayoutRepeatPassword.error = "Password doesn't match!"
                } else {
                    textInputLayoutRepeatPassword.error = null
                }
            }
        }
        return flag
    }
}