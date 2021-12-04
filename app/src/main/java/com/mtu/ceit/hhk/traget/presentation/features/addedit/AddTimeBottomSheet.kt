package com.mtu.ceit.hhk.traget.presentation.features.addedit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mtu.ceit.hhk.traget.databinding.BottomsheetTimepickerBinding

class AddTimeBottomSheet: BottomSheetDialogFragment() {


    lateinit var binding:BottomsheetTimepickerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = BottomsheetTimepickerBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hour = 0
        var minute = 0

        binding.dialogTp.setIs24HourView(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.dialogTp.hour = 0
            binding.dialogTp.minute = 0
        }
        else{
            binding.apply {
                dialogTp.currentHour = 0
                dialogTp.currentMinute = 0
            }
        }

        binding.dialogTp.setOnTimeChangedListener { _, hr, min ->

            hour = hr
            minute = min
        }

        binding.dialogTpApplyBtn.setOnClickListener {
            timeChangeListener.invoke(hour,minute)
            this.dismiss()
        }

        binding.dialogTpCancelBtn.setOnClickListener {
            this.dismiss()
        }


    }

    companion object {
        lateinit var timeChangeListener:(Int,Int) -> Unit
        val TAG = "ADD TIME BOTTOMSHEET"
    }

}