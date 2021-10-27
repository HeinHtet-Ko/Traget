package com.mtu.ceit.hhk.traget.ui.clients

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentClientsBinding
import com.mtu.ceit.hhk.traget.repos.DISPLAY_STATUS
import com.mtu.ceit.hhk.traget.repos.SORT
import com.mtu.ceit.hhk.traget.ui.adapter.ClientAdapter
import com.mtu.ceit.hhk.traget.util.onQueryChanged
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ClientFragment :Fragment(R.layout.fragment_clients) {


    lateinit var binding:FragmentClientsBinding
    lateinit var clientAdapter: ClientAdapter


    val clientVM: ClientViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)


        clientVM.sort.value = SORT.SortByDate
        clientAdapter = ClientAdapter()

        binding.frClientsRecycler.apply {
            adapter = clientAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)

        }

        clientAdapter.longClick = {
            clientVM.removeClient(it)

        }

        clientAdapter.click = {
            val action = ClientFragmentDirections.actionClientToAddEditClientFragment(it)
            findNavController().navigate(action)
        }

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val client = clientAdapter.currentList[viewHolder.adapterPosition]
                when(direction) {
                    ItemTouchHelper.LEFT -> {

                        clientVM.onSwipeLeft(client.id)
                    }
                    ItemTouchHelper.RIGHT -> {

                        clientVM.onSwipeRight(client.id)
                    }
                }
                Toast.makeText(requireContext(),"Siwpe",Toast.LENGTH_LONG).show()

//                clientAdapter.notifyDataSetChanged()
            }

        }).attachToRecyclerView(binding.frClientsRecycler)



        binding.frClientsFab.setOnClickListener {

            val action = ClientFragmentDirections.actionClientToAddEditClientFragment(null)
            findNavController().navigate(action)

        }


        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        clientVM._clients.observe(viewLifecycleOwner){

            clientAdapter.submitList(it)
        }

    }

    fun observeHide(menu: Menu){

        clientVM.displayStatus.observe(viewLifecycleOwner) {
            when (it) {
                DISPLAY_STATUS.HIDE_PAID -> {
                    menu.findItem(R.id.action_hide_paid).isChecked = true
                    clientVM.hideUnPaid(false)
                    clientVM.hidePaid(true)
                }
                DISPLAY_STATUS.HIDE_UNPAID ->{
                    menu.findItem(R.id.action_hide_unpaid).isChecked = true
                    clientVM.hidePaid(false)
                    clientVM.hideUnPaid(true)
                }
                DISPLAY_STATUS.SHOW_ALL ->{
                menu.findItem(R.id.action_show_all).isChecked = true
                    clientVM.hidePaid(false)
                    clientVM.hideUnPaid(false)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu,menu)

        val search = menu.findItem(R.id.search_menu)
        val searchView = search.actionView as SearchView

        searchView.onQueryChanged {
            clientVM.searchQuery.value = it
        }

        observeHide(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_sort_by_amt_desc -> {
                clientVM.sort.value = SORT.SortByAmtDesc
                true
            }
            R.id.action_sort_by_amt_asc -> {
                clientVM.sort.value = SORT.SortByAmtAsc
                true
            }
            R.id.action_sort_by_date -> {
                clientVM.sort.value = SORT.SortByDate
                true
            }
            R.id.action_hide_paid -> {
                item.isChecked = !item.isChecked
                clientVM.displayStatus.value = DISPLAY_STATUS.HIDE_PAID
                true
            }
            R.id.action_hide_unpaid -> {
                item.isChecked = !item.isChecked
                clientVM.displayStatus.value = DISPLAY_STATUS.HIDE_UNPAID
                true
            }
            R.id.action_show_all -> {
                item.isChecked = !item.isChecked
                clientVM.displayStatus.value = DISPLAY_STATUS.SHOW_ALL
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}