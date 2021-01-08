package com.mirkamal.gamewatch.ui.fragments.splash

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.utils.EXTRA_SKIP_SPLASH

/**
 * Created by Mirkamal on 17 October 2020
 */
class SplashFragment : Fragment() {

    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser
        handler = Handler(Looper.getMainLooper())

        configureStatusBarColor()

        val skip = activity?.intent?.getBooleanExtra(EXTRA_SKIP_SPLASH, false)

        if (skip == true) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        } else {
            if (user == null) {
                //Not logged in
                //Navigate to intro
                handler.postDelayed({
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToIntroContainerFragment())
                }, 500)
            } else {
                //Logged in
                //Navigate to main part
                handler.postDelayed({
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHostFragment())
                }, 500)
            }
        }
    }

    private fun configureStatusBarColor() {
        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> activity?.window?.statusBarColor = Color.parseColor("#00000000")
            Configuration.UI_MODE_NIGHT_UNDEFINED -> activity?.window?.statusBarColor = Color.parseColor("#8a0035")
        }
    }
}