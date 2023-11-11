package com.cube.cubeacademy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cube.cubeacademy.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface BackModalClickListener {
    fun onLeavePageButtonClick()
    fun onCancelButtonClick()
}
class BackModalFragment : BottomSheetDialogFragment() {

    private var clickListener: BackModalClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_back_modal, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set expanded Bottom Sheet behavior
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }

        // leave page and cancel Buttons
        val leavePageButton = view.findViewById<Button>(R.id.leavePageButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)

        leavePageButton.setOnClickListener {
            clickListener?.onLeavePageButtonClick()
        }

        cancelButton.setOnClickListener {
            clickListener?.onCancelButtonClick()
            dismiss()
        }
    }

    fun setClickListener(listener: BackModalClickListener) {
        clickListener = listener
    }
}