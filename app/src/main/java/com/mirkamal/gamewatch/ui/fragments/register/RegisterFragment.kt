package com.mirkamal.gamewatch.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        setOnClickListener()
    }

    private fun setOnClickListener() {
        buttonSignUp.setOnClickListener {
            if (validateFields()) {
                overlayLayout.visibility = View.VISIBLE

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
        overlayLayout.visibility = View.INVISIBLE
    }

    private fun validateFields(): Boolean {
        var flag: Boolean
        val email = textInputEditTextEmail.text.toString()
        val password = textInputEditTextPassword.text.toString()
        val rPassword = textInputEditTextRepeatPassword.text.toString()

        if (email.isEmpty()) {
            textInputEditTextEmail.error = "Field is empty!"
        } else {
            flag = Validator.validateEmail(email)
            if (!flag) {
                textInputEditTextEmail.error = "Email is invalid!"
            }
        }

        if (password.isEmpty()) {
            flag = false
            textInputEditTextPassword.error = "Field is empty!"
        } else {
            flag = Validator.validatePassword(password)
            if (!flag) {
                textInputEditTextPassword.error = "Email is invalid!"
            } else {
                if (password != rPassword) {
                    flag = false
                    textInputEditTextRepeatPassword.error = "Password doesn't match!"
                }
            }
        }
        return flag
    }
}