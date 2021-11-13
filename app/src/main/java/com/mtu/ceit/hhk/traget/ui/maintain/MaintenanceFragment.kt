package com.mtu.ceit.hhk.traget.ui.maintain

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentMaintenanceBinding
import com.mtu.ceit.hhk.traget.ui.maintain.addEditMaintain.AddEditMaintainBottomSheet
import com.mtu.ceit.hhk.traget.util.DialogBuilder
import com.mtu.ceit.hhk.traget.util.MAIN_EVENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MaintenanceFragment:Fragment(R.layout.fragment_maintenance) {



    val vm: MaintenanceViewModel by activityViewModels()
    private val addEditbottomsheet by lazy {
       AddEditMaintainBottomSheet()
    }
    private var adapter: MaintainAdapter = MaintainAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding: FragmentMaintenanceBinding = FragmentMaintenanceBinding.bind(view)

        viewInit(binding)

        collectEvents()




    }


    private fun collectEvents(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

                launch {
                    vm.maintainsF.collect {
                        adapter.submitList(it)
                    }
                }

                launch {
                    vm.mainEventFlow.collect {
                        when(it) {
                            MAIN_EVENT.SHOW_DIALOG ->{
                                if(!addEditbottomsheet.isAdded)
                                    addEditbottomsheet.show(parentFragmentManager,AddEditMaintainBottomSheet.TAG)
                            }
                            MAIN_EVENT.HIDE_DIALOG -> addEditbottomsheet.dismiss()

                        }
                    }
                }



            }
        }
    }

    private fun viewInit(binding: FragmentMaintenanceBinding){


        adapter.itemClickListen = {
            vm.onMaintainItemClick(it)
        }

        adapter.itemLongClickListen = {

            DialogBuilder.buildAlertDialog(
                    requireContext(),
                    {
                        vm.onMaintainItemLongClick(it)
                    },
                    getString(R.string.maintain_delete_alert),
                    R.drawable.delete_item
            )

        }

        binding.apply {



            frMaintainFab.setOnClickListener {
                vm.onFABClick()
            }

            frMaintainRecycler.adapter = adapter
            frMaintainRecycler.layoutManager =  LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)
        }




    }


}