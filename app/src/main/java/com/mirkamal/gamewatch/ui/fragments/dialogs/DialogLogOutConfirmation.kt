package com.mirkamal.gamewatch.ui.fragments.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.ui.activities.main.MainActivity
import com.mirkamal.gamewatch.utils.EXTRA_SKIP_SPLASH
import kotlinx.android.synthetic.main.dialog_log_out_confirmation.*

/**
 * Created by Mirkamal on 04 January 2021
 */
class DialogLogOutConfirmation: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_log_out_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewYes.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra(EXTRA_SKIP_SPLASH, true)
            startActivity(intent)
            activity?.finish()
        }
        cardViewNo.setOnClickListener {
            dismiss()
        }
    }
}