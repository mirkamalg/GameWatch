package com.mirkamal.gamewatch.ui.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mirkamal.gamewatch.R
import kotlinx.android.synthetic.main.dialog_upload_photo.*

/**
 * Created by Mirkamal on 22 December 2020
 */
class BottomSheetUploadPhoto : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_upload_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewContainerProfile.setOnClickListener {
            dismiss()
        }

        cardViewContainerCover.setOnClickListener {
            dismiss()
        }
    }
}