package com.mtu.ceit.hhk.traget.presentation.features.home


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.mtu.ceit.hhk.traget.R

import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.databinding.FragmentHomeBinding

import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {


@Inject
lateinit var chartDrawer: ChartDrawer


private val vm: HomeViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)


        setLabelColors(binding)

        vm.paidSum.observe(viewLifecycleOwner){

            val sum = it?:0
            val str = getString(R.string.home_paidSum_str,sum)
            binding.frHomePaidsumTv.text = str

        }

        vm.unPaidSum.observe(viewLifecycleOwner){
            val sum = it?: 0
            val str = getString(R.string.home_unPaidSum_str,sum)
            binding.frHomeUnpaidsumTv.text = str
        }

        vm.dieselSum.observe(viewLifecycleOwner) {
            val sum = it?:0
            val str = getString(R.string.home_dieselSum_str,sum)
            binding.frHomeDieselsumTv.text = str
        }

        vm.mainTainSum.observe(viewLifecycleOwner) {
            val sum = it?:0
            val str = getString(R.string.home_maintainSum_str,sum)
            binding.frHomeMaintainsumTv.text = str


        }


        vm.rvSum.observe(viewLifecycleOwner) {


            val min = it?: 0
            val pair =  Utils.formateDate(min)
            val str = getString(R.string.home_rv_time,pair.first.toInt(),pair.second.toInt())
            binding.frHomeRvsumTv.text = str


        }

        vm.hrSum.observe(viewLifecycleOwner) {

                val min = it?:0
                val pair =  Utils.formateDate(min)
                val str = getString(R.string.home_hr_time,pair.first.toInt(),pair.second.toInt())
                binding.frHomeHrsumTv.text = str
        }


        vm.combineAmt().observe(viewLifecycleOwner){
            chartDrawer.drawBarChart(it,binding.frHomeBarchart,requireContext())
        }

        vm.combineTime().observe(viewLifecycleOwner){
            chartDrawer.drawPieChart(it,binding.frHomePiechart,requireContext())
        }


    }

    private fun setLabelColors(binding: FragmentHomeBinding){

        binding.apply {
            paidLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[0])
            unpaidLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[1])
            maintainLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[2])
            dieselLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[3])

            rvLegend.setBackgroundColor(ColorTemplate.PASTEL_COLORS[0])
            hrLegend.setBackgroundColor(ColorTemplate.PASTEL_COLORS[1])

        }
    }


}