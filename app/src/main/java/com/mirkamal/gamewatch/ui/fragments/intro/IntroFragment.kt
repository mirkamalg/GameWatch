package com.mirkamal.gamewatch.ui.fragments.intro

import android.graphics.Typeface
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

        configureUI()
    }

    private fun configureUI() {

        val index = (parentFragment as IntroContainerFragment)
            .getIndex(this)

        when(index) {
            0 -> {
                imageViewIntro.setImageResource(R.drawable.drawable_intro_background_1)
                textViewIntro.text = context?.getString(R.string.info_intro_1)
            }
            1 -> {
                imageViewIntro.setImageResource(R.drawable.drawable_intro_background_2)
                textViewIntro.text = getString(R.string.info_intro_2)
            }
            2 -> {
                imageViewIntro.setImageResource(R.drawable.drawable_intro_background_3)
                textViewIntro.text = getString(R.string.info_intro_3)
                buttonLetsGo.visibility = View.VISIBLE
            }
        }

        val typeface = Typeface.createFromAsset(activity?.assets, "fonts/PT_Sans/PTSans-Regular.ttf")
        textViewIntro.typeface = typeface

    }
}