package com.mirkamal.gamewatch.ui.fragments.forgot_password

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.Validator
import kotlinx.android.synthetic.main.fragment_forgot_password.*

/**
 * Created by Mirkamal on 09 January 2021
 */
class ForgotPasswordFragment : Fragment() {

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
        configureValidation()
    }

    private fun configureValidation() {
        textInputEditTextEmail.doAfterTextChanged {
            if (!Validator.validateEmail(it.toString())) {
                textInputLayoutEmail.error = "Email is invalid"
            } else {
                textInputLayoutEmail.error = null
            }
        }
    }

    private fun setOnClickListener() {
        buttonResetPassword.setOnClickListener {
            if (textInputLayoutEmail.error == null) {
                overlayLayout.isVisible = true
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

                auth.sendPasswordResetEmail(textInputEditTextEmail.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(context, "Email has been sent!", Toast.LENGTH_SHORT).show()
                        overlayLayout.isVisible = false
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        overlayLayout.isVisible = false
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }

            }
        }
        buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}