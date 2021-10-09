package com.mtu.ceit.hhk.traget.ui.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentClientsBinding
import com.mtu.ceit.hhk.traget.ui.adapter.ClientAdapter
import com.mtu.ceit.hhk.traget.ui.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientFragment :Fragment(R.layout.fragment_clients) {


    lateinit var binding:FragmentClientsBinding
    lateinit var adapter: ClientAdapter
    lateinit var clientVM: ClientViewModel


    lateinit var dialogFragment: AddClientFragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)


        dialogFragment = AddClientFragment()
        clientVM =( requireActivity() as MainActivity).clientVM

        adapter = ClientAdapter{
            clientVM.removeClient(it)

        }
        binding.clientRecycler.adapter = adapter

        clientVM.getAllClients()



        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val client = adapter.currentList[viewHolder.adapterPosition]
                when(direction) {
                    ItemTouchHelper.LEFT -> {

                        clientVM.onSwipeLeft(client.id)
                    }
                    ItemTouchHelper.RIGHT -> {

                        clientVM.onSwipeRight(client.id)
                    }
                }
                Toast.makeText(requireContext(),"Siwpe",Toast.LENGTH_LONG).show()

                adapter.notifyDataSetChanged()
            }

        }).attachToRecyclerView(binding.clientRecycler)

        clientVM.clients.observe(viewLifecycleOwner){

            adapter.submitList(it)
        }

        binding.addFAB.setOnClickListener {
            dialogFragment.show(requireActivity().supportFragmentManager,"ADD CLIENT")
        }


        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu,menu)
    }

}