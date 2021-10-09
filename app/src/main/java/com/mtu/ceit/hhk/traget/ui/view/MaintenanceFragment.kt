package com.mtu.ceit.hhk.traget.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentMaintenanceBinding
import com.mtu.ceit.hhk.traget.ui.adapter.AppAdapter
import com.mtu.ceit.hhk.traget.ui.viewmodel.ApplianceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MaintenanceFragment:Fragment(R.layout.fragment_maintenance) {


    lateinit var binding: FragmentMaintenanceBinding
    private val vm:ApplianceViewModel by viewModels()
    lateinit var adapter: AppAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AppAdapter()
        binding = FragmentMaintenanceBinding.bind(view)
        binding.appRecycler.adapter = adapter

        vm.getApp()

        vm.appliances.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Log.d("alcohol", "onViewCreated: ${adapter.currentList.size} haha")
        }


    }

}