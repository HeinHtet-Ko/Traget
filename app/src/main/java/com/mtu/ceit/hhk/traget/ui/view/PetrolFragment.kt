package com.mtu.ceit.hhk.traget.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.data.model.Barrel
import com.mtu.ceit.hhk.traget.data.model.BarrelWithClients
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.databinding.FragmentPetrolBinding
import com.mtu.ceit.hhk.traget.ui.adapter.BarrelAdapter
import com.mtu.ceit.hhk.traget.ui.viewmodel.BarrelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PetrolFragment:Fragment(R.layout.fragment_petrol) {

    private val vm by viewModels<BarrelViewModel>()

    lateinit var bAdapter:BarrelAdapter
    lateinit var binding:FragmentPetrolBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       binding = FragmentPetrolBinding.bind(view)
        vm.getBarrelsWithClients()

    //    lifecycleScope.launch {
  //          vm.getBarrels().join()
            vm.barrels.observe(viewLifecycleOwner){

                Timber.tag("barrel").e("${it.size}")
                bAdapter.submitList(it)
            }
//        }


        bAdapter = BarrelAdapter{  barrel ->
            vm.barrelWithClients.observe(viewLifecycleOwner) {
Toast.makeText(requireContext(),getClientList(barrel.bId,it).size.toString(),Toast.LENGTH_LONG).show()
            }
        }
        binding.barrelRecycler.adapter = bAdapter




        binding.addBarrel.setOnClickListener {
            vm.buyBarrel(Barrel(price = 300000))
        }


    }

    private fun getClientList(i:Int, list:List<BarrelWithClients>):List<Client>{

        for (item:BarrelWithClients in list) {
            if(item.barrel.bId == i ) return item.clientList
        }
        return emptyList()
    }


}