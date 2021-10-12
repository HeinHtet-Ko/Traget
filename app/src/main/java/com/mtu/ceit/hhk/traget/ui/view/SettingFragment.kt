package com.mtu.ceit.hhk.traget.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.data.NightPrefImpl
import com.mtu.ceit.hhk.traget.databinding.FragmentSettingBinding
import com.mtu.ceit.hhk.traget.ui.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment:Fragment(R.layout.fragment_setting) {



    lateinit var binding:FragmentSettingBinding
    private val vm by viewModels<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingBinding.bind(view)



        binding.darkTog.setOnCheckedChangeListener { _, b ->
            vm.setNightMode(b)
        }

        vm.nightFlow.observe(viewLifecycleOwner){

            if(it) {

                binding.darkTog.isChecked = it
            }else {

                binding.darkTog.isChecked = it

            }}


    }





}