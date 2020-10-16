package com.mirkamal.gamewatch.ui.fragments.login

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.Validator
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * Created by Mirkamal on 16 October 2020
 */
class LoginFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextFont()
        setOnClickListeners()
    }

    private fun setTextFont() {
        val typeface = Typeface.createFromAsset(activity?.assets, "fonts/PT_Sans/PTSans-Italic.ttf")
        textViewRegister.typeface = typeface
    }

    private fun setOnClickListeners() {
        buttonLogin.setOnClickListener {
            if (validateFields()) Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFields(): Boolean {
        return if (Validator.validateEmail(textInputEditTextEmail.text.toString())) {
            if (Validator.validatePassword(textInputEditTextPassword.text.toString())) {
                true
            } else {
                textInputEditTextPassword.error = "Password is invalid"
                false
            }
        }else {
            textInputEditTextEmail.error = "Email is invalid"
            if (!Validator.validatePassword(textInputEditTextPassword.text.toString())) {
                textInputEditTextPassword.error = "Password is invalid"
            }
            false
        }
    }
}