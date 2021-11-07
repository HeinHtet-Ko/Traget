package com.mtu.ceit.hhk.traget.ui.diesel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentDieselBinding
import com.mtu.ceit.hhk.traget.ui.diesel.addEditDiesel.AddEditDieselBottomSheet
import com.mtu.ceit.hhk.traget.util.MAIN_EVENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DieselFragment:Fragment(R.layout.fragment_diesel) {

    private val vm by activityViewModels<DieselViewModel>()
    lateinit var binding:FragmentDieselBinding
    var dcAdapter:DieselWithClientAdapter = DieselWithClientAdapter()
    private val addEditbs by lazy {
        AddEditDieselBottomSheet()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentDieselBinding.bind(view)
        binding.barrelRecycler.adapter = dcAdapter
        binding.barrelRecycler.itemAnimator = DefaultItemAnimator()

        vm.getBarrelsWithClients()

        binding.frDieselFab.setOnClickListener {

            vm.onFabClick()
        }

        dcAdapter.itemLongClick={ id ->
            vm.setActiveId(id)
       }

        vm.barrelWithClients.observe(viewLifecycleOwner){  dieselsWithClients ->

            val ls = dieselsWithClients.map { DieselWithClientModel.Parent_Diesel(it) }
            dcAdapter.itemList = ls.toMutableList()

        }


       collectEvents()



    }

    private fun collectEvents(){
        lifecycleScope.launch {

            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.mainEventFlow.collect {
                    when(it) {
                        is MAIN_EVENT.SHOW_DIALOG ->{
                            if(!addEditbs.isAdded)
                                addEditbs.show(parentFragmentManager,AddEditDieselBottomSheet.TAG)
                        }
                        is MAIN_EVENT.HIDE_DIALOG -> addEditbs.dismiss()
                    }
                }
            }

        }
    }


}