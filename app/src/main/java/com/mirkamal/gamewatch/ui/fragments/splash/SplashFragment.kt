package com.mirkamal.gamewatch.ui.fragments.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R

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

        if (user == null) {
            //Not logged in
            //Navigate to intro
            handler.postDelayed({
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToIntroContainerFragment())
            }, 2000)
        } else {
            //Logged in
            //Navigate to main part
            handler.postDelayed({
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToIntroContainerFragment())
            }, 2000)
        }


    }
}