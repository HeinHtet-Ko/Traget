package com.mtu.ceit.hhk.traget.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentClientsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientFragment:Fragment(R.layout.fragment_clients) {


    lateinit var binding:FragmentClientsBinding
    lateinit var adapter: ClientAdapter
    lateinit var clientVM:ClientViewModel


    lateinit var dialogFragment: AddClientFragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)

        adapter = ClientAdapter()
        dialogFragment = AddClientFragment()
        clientVM =( requireActivity() as MainActivity ).clientVM

        binding.clientRecycler.adapter = adapter

        clientVM.getAllClients()




        clientVM.clients.observe(viewLifecycleOwner){

            adapter.submitList(it)
        }

        binding.addFAB.setOnClickListener {
            dialogFragment.show(requireActivity().supportFragmentManager,"ADD CLIENT")
        }


    }

}