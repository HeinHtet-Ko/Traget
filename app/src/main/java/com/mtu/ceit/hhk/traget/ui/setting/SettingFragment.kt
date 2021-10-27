package com.mtu.ceit.hhk.traget.ui.setting

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentSettingBinding
import com.mtu.ceit.hhk.traget.ui.MainActivity
import com.mtu.ceit.hhk.traget.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SettingFragment:Fragment(R.layout.fragment_setting) {



    lateinit var binding:FragmentSettingBinding
    private lateinit var vm :SettingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingBinding.bind(view)


        vm = (requireActivity() as MainActivity).settingVM
        binding.frSettingDarkSw.setOnCheckedChangeListener { _, b ->
            vm.setNightMode(b)
        }


        vm.isNight.observe(viewLifecycleOwner){

            if(it) {

                binding.frSettingDarkSw.isChecked = it
            }else {

                binding.frSettingDarkSw.isChecked = it

            }}

        binding.apply {

            vm.hrPrice.observe(viewLifecycleOwner){
                frSettingHrPriceEdText.text = Editable.Factory().newEditable(it.toString())
            }
            vm.rvPrice.observe(viewLifecycleOwner){
                frSettingRvPriceEdText.text = Editable.Factory().newEditable(it.toString())
            }


            frSettingPriceEditBtn.setOnClickListener {
                frSettingRvPriceEdText.isEnabled = true
                frSettingHrPriceEdText.isEnabled = true
            }


            frSettingPriceApplyBtn.setOnClickListener {

               val rvPrice = frSettingRvPriceEdText.text.toString().toInt()
               val hrPrice = frSettingHrPriceEdText.text.toString().toInt()

               vm.setHrPrice(hrPrice)
               vm.setRvPrice(rvPrice)

                frSettingRvPriceEdText.isEnabled = false
                frSettingHrPriceEdText.isEnabled = false

            }


        }


    }





}