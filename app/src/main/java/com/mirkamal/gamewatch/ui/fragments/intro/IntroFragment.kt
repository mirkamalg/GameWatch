package com.mirkamal.gamewatch.ui.fragments.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirkamal.gamewatch.R
import kotlinx.android.synthetic.main.fragment_intro.*

/**
 * Created by Mirkamal on 16 October 2020
 */
class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pageType = (parentFragment as IntroContainerFragment)
            .getType(this)
        imageViewIntro.setImageResource(pageType.imageResource)
        textViewIntro.text = pageType.text

        if (pageType != IntroContainerFragment.IntroPageType.INTRO_3) {
            buttonLetsGo.visibility = View.GONE
        }
    }
}