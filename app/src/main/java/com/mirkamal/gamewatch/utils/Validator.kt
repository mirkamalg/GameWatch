package com.mirkamal.gamewatch.utils

import java.util.regex.Pattern

/**
 * Created by Mirkamal on 17 October 2020
 */
class Validator {

    companion object {
        fun validateEmail(email: String): Boolean {
            val emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
            val pattern = Pattern.compile(emailPattern)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun validatePassword(password: String): Boolean {
            val emailPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
            val pattern = Pattern.compile(emailPattern)
            val matcher = pattern.matcher(password)
            return matcher.matches()
        }
    }
}