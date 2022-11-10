package com.example.exchangeofhandymen.presenter.home.homeNavigation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.exchangeofhandymen.R
import kotlinx.android.synthetic.main.dialog_woker.view.*

class WorkerDialogFragment :DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView:View=inflater.inflate(R.layout.dialog_woker,null,false)

        rootView.btn_woker.setOnClickListener {
           parentFragmentManager.setFragmentResult("Dialog_rezult", bundleOf("dialog_key" to true))
            dismiss()
        }

        rootView.btn_employer.setOnClickListener {
            parentFragmentManager.setFragmentResult("Dialog_rezult", bundleOf("dialog_key" to false))
            dismiss()
        }


        return rootView
    }

    override fun getTheme(): Int =R.style.RoundedCornersDialog


}