package com.mtu.ceit.hhk.traget.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.mtu.ceit.hhk.traget.R

import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.databinding.FragmentHomeBinding

import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {


lateinit var binding: FragmentHomeBinding

private val vm: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)


        vm.paidSum.observe(viewLifecycleOwner){

            val sum = it?:0
            binding.frHomePaidsumTv.text = "$sum Ks"

        }

        vm.unPaidSum.observe(viewLifecycleOwner){
          val sum = it?: 0
          binding.frHomeUnpaidsumTv.text = "$sum Ks"
        }

        vm.dieselSum.observe(viewLifecycleOwner) {
            val sum = it?:0
            binding.frHomeDieselsumTv.text = "$sum Ks"
        }

        vm.mainTainSum.observe(viewLifecycleOwner) {
            val sum = it?:0
            binding.frHomeMaintainsumTv.text = "$sum Ks"


        }



        vm.rvSum.observe(viewLifecycleOwner) {


            val min = it?: 0
            val pair =  Utils.formateDate(min)
            binding.frHomeRvsumTv.text = " ${pair.first} : ${pair.second}"


        }

        vm.hrSum.observe(viewLifecycleOwner) {

                val min = it?:0
                val pair =  Utils.formateDate(min)
                binding.frHomeHrsumTv.text = " ${pair.first} : ${pair.second}"
        }




    }

    override fun onResume() {
        super.onResume()
        vm.combineAmt().observe(viewLifecycleOwner){
            barChartSet(it)
            }

        vm.combineTime().observe(viewLifecycleOwner){
                pieChartSet(it)
        }

    }



    private fun barChartSet(values:ArrayList<Int?>){

        val ents = arrayListOf<BarEntry>()
        val legendEntries = arrayListOf<LegendEntry>()
        val labels = arrayOf("Paid","Unpaid","Main","Oil")

        Timber.tag("timberlog").e("OnBarSet ${values.size}")

        for((ind, i:Int?) in values.withIndex()) {

                ents.add(BarEntry((ind + 1).toFloat(),(i?:0).toFloat()))

                val legendEntry = LegendEntry()

                legendEntry.label = labels[ind]
                legendEntry.formSize = 15f
                legendEntry.formColor = ColorTemplate.MATERIAL_COLORS[ind]
                legendEntries.add(legendEntry)
            }


        val barChart = binding.frHomeBarchart
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false


        barChart.legend.apply {
            setCustom(legendEntries)
            isWordWrapEnabled = true
            xEntrySpace = 100f
            yEntrySpace = 10f
            textSize = 20f

        }

        val title = "Title"

        val dataset = BarDataSet(ents,title)
        dataset.valueTextSize = 15f

        val tv = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.barChartLabelColor, tv,true)
        dataset.valueTextColor = tv.data

       val leftY = barChart.axisLeft
        val  xAxis    = barChart.xAxis
        xAxis.setDrawLabels(false)
        leftY.setDrawGridLines(false)
        leftY.setDrawLabels(false)

        dataset.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val data = BarData(dataset)
        barChart.animateY(1000)
        barChart.data = data
        Log.d("fiasco", "barchart ")
        barChart.invalidate()

        binding.apply {
            paidLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[0])
            unpaidLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[1])
            maintainLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[2])
            dieselLegend.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[3])

        }

    }



    private fun pieChartSet(values:ArrayList<Int?>) {

        val labels = arrayOf("Dollar","LinPan")
        val pies = arrayListOf<PieEntry>()


        for((i,value) in values.withIndex()){
            val ent = PieEntry((value?:0).toFloat(),labels[i])
            pies.add(ent)

        }

        val dataset = PieDataSet(pies,"Total Time")
        dataset.colors = ColorTemplate.PASTEL_COLORS.toList()
        dataset.apply {

            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            valueTextSize = 20f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                valueTypeface = requireActivity().resources.getFont(R.font.bf)
            }
            else{
                valueTypeface = ResourcesCompat.getFont(requireContext(),R.font.bf)
            }



            dataset.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
               val pair = Utils.formateDate(value.toInt())
                "${pair.first}:${pair.second}"
            }


            val tvlb = TypedValue()
            requireContext().theme.resolveAttribute(R.attr.barChartLabelColor, tvlb,true)
            dataset.valueTextColor = tvlb.data

        }
        val pData = PieData(dataset)
        binding.frHomePiechart.apply {
            data = pData

            val tv = TypedValue()
            requireContext().theme.resolveAttribute(R.attr.pieHoleColor, tv,true)
            setHoleColor(tv.data)
           // isDrawHoleEnabled = false
            setExtraOffsets(30f,20f,30f,20f)
            setEntryLabelTextSize(20f)
            setDrawEntryLabels(false)
            legend.isEnabled = false
            description.isEnabled = false
            animateXY(2000,2000)
            invalidate()


        }

        binding.apply {
            rvLegend.setBackgroundColor(ColorTemplate.PASTEL_COLORS[0])
            hrLegend.setBackgroundColor(ColorTemplate.PASTEL_COLORS[1])
        }


    }

}