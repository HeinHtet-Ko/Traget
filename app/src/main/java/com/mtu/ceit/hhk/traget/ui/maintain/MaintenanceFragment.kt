package com.mtu.ceit.hhk.traget.ui.maintain

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentMaintenanceBinding
import com.mtu.ceit.hhk.traget.ui.adapter.MaintainAdapter
import com.mtu.ceit.hhk.traget.ui.maintain.addEditMaintain.AddEditMaintainBottomSheet
import com.mtu.ceit.hhk.traget.util.MAIN_EVENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MaintenanceFragment:Fragment(R.layout.fragment_maintenance) {



    val vm: MaintenanceViewModel by activityViewModels()
    private val addEditbottomsheet = AddEditMaintainBottomSheet()
     private var adapter: MaintainAdapter = MaintainAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding: FragmentMaintenanceBinding = FragmentMaintenanceBinding.bind(view)
        binding.frMaintainRecycler.adapter = adapter

        binding.frMaintainRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)


        addEditbottomsheet.isCancelable = false

        vm.getMaintainList()

        vm.maintains.observe(viewLifecycleOwner) {
            adapter.submitList(it)

            binding.frMaintainRecycler.scrollToPosition(it.size-3)

        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

                vm.mainEventFlow.collect {
                    when(it) {
                        is MAIN_EVENT.SHOW_DIALOG -> addEditbottomsheet.show(parentFragmentManager,AddEditMaintainBottomSheet.TAG)
                        is MAIN_EVENT.HIDE_DIALOG -> addEditbottomsheet.dismiss()

                    }
                }

            }
        }

        binding.frMaintainFab.setOnClickListener{

            vm.onFABClick()

        }

        adapter.itemClickListen = {

            vm.onMaintainItemClick(it)

        }
    }


}