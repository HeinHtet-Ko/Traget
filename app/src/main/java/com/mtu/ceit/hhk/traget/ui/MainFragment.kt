package com.mtu.ceit.hhk.traget.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentMainBinding

class MainFragment:Fragment(R.layout.fragment_main) {

lateinit var binding: FragmentMainBinding
lateinit var vm:ClientViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)

        vm =( requireActivity() as MainActivity ).clientVM

        vm.getUnpaidSum()

      vm._paidSum.observe(viewLifecycleOwner){

          binding.paidSum.text = it.total.toString()
      }






    }

}