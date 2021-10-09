package com.mtu.ceit.hhk.traget.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.mtu.ceit.hhk.traget.R

import com.mtu.ceit.hhk.traget.Utils
import com.mtu.ceit.hhk.traget.databinding.FragmentMainBinding
import com.mtu.ceit.hhk.traget.ui.view.MainActivity
import com.mtu.ceit.hhk.traget.ui.viewmodel.ClientViewModel
import com.mtu.ceit.hhk.traget.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DateFormat
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_main) {


lateinit var binding: FragmentMainBinding

      private val vm: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {




        Timber.tag("timberlog").e("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)







      vm.unPaidSum.observe(viewLifecycleOwner){

          val sum = it?: 0
          binding.UnpaidSum.text = sum.toString()



      }

        vm.paidSum.observe(viewLifecycleOwner){
            val sum = it?:0
            binding.paidSum.text = sum.toString()

        }

        vm.barrelSum.observe(viewLifecycleOwner) {
            val sum = it?:0
            binding.barrelPrice.text = sum.toString()
        }

        vm.maintenanceSum.observe(viewLifecycleOwner) {
            val sum = it?:0
            binding.totalMain.text = sum.toString()


        }



        vm.dollarSum.observe(viewLifecycleOwner) {


             val min = it?: 0
              val pair =  Utils.formateDate(min)
                binding.totalD.text = " ${pair.first} : ${pair.second}"


        }

        vm.linpanSum.observe(viewLifecycleOwner) {

            Timber.tag("timberlog").e("linpanSum")

            val min = it?:0


                val pair =  Utils.formateDate(min)
                binding.totalL.text = " ${pair.first} : ${pair.second}"


        }


        Timber.tag("timberlog").e("OnViewCreated")

    }

    override fun onResume() {
        super.onResume()

            vm.combineListen().observe(viewLifecycleOwner){

                Timber.tag("timberlog").e("OnResumeOB ${it[0]}")
                barChartSet(it)
            }


        Timber.tag("timberlog").e("OnResume")
    }



    private fun barChartSet(values:ArrayList<Int?>){

        val ents = arrayListOf<BarEntry>()

        Timber.tag("timberlog").e("OnBarSet ${values.size}")

        for((ind, i:Int?) in values.withIndex()) {

                ents.add(BarEntry((ind + 1).toFloat(),(i?:0).toFloat()))
            }


        val barChart = binding.barChart

        val title = "Title"

        val dataset = BarDataSet(ents,title)
        dataset.valueTextSize = 15f

       val leftY = barChart.axisLeft
        val  xAxis    = barChart.xAxis
        xAxis.setDrawLabels(false)
        leftY.setDrawGridLines(false)
        leftY.setDrawLabels(false)

        dataset.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val data = BarData(dataset)
        barChart.animateY(3000)
        barChart.data = data
        Log.d("fiasco", "barchart ")
        barChart.invalidate()

    }

}