package com.mtu.ceit.hhk.traget.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.AddClientBinding
import com.mtu.ceit.hhk.traget.ui.viewmodel.ClientViewModel


class AddClientFragment:BottomSheetDialogFragment() {

    lateinit var binding: AddClientBinding
     lateinit var  clientViewModel: ClientViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = AddClientBinding.inflate(inflater,container,false)
        binding.tPicker.setIs24HourView(true)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clientViewModel = (requireActivity() as MainActivity).clientVM

        var time:Int = 0
        var amt:Int = 0
        var mac:String = ""
        var name:String = ""
        var bID:Int = 0
        binding.addBtn.setOnClickListener {


            name = binding.nameeditText.text.toString()
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    time = binding.tPicker.hour*60
                    time += binding.tPicker.minute


                }else{
                    time = binding.tPicker.currentHour*60
                    time += binding.tPicker.currentMinute
                }
            when(binding.chipGroup.checkedChipId){
                R.id.dollarChip ->
                {
                    mac = "D"
                    amt = time * 500
                }
                R.id.lpChip ->
                {
                    mac = "L"
                    amt = time * 250
                }
            }
            bID = binding.barrelNoInput.text.toString().toInt()
            clientViewModel.onSumbitClick(name,time,amt,mac,bID)

            this.dismiss()
        }
    }

}