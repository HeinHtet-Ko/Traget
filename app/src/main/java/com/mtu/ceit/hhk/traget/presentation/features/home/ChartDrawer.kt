package com.mtu.ceit.hhk.traget.presentation.features.home

import android.content.Context
import android.util.TypedValue
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.util.Utils
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject


@Module
@InstallIn(ActivityComponent::class)
class ChartDrawer @Inject constructor() {

     fun drawBarChart(values:ArrayList<Int?>,barChart: BarChart,context: Context){

        val ents = arrayListOf<BarEntry>()
        val legendEntries = arrayListOf<LegendEntry>()
        val labels = arrayOf("Paid","Unpaid","Maintain","Diesel")

        for((ind, i:Int?) in values.withIndex()) {

            ents.add(BarEntry((ind + 1).toFloat(),(i?:0).toFloat()))

            val legendEntry = LegendEntry()

            legendEntry.label = labels[ind]
            legendEntry.formSize = 15f
            legendEntry.formColor = ColorTemplate.MATERIAL_COLORS[ind]
            legendEntries.add(legendEntry)
        }


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
        context.theme.resolveAttribute(R.attr.barChartLabelColor, tv,true)
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
        barChart.invalidate()

     }


    fun drawPieChart(values:ArrayList<Int?>,pieChart: PieChart,context: Context) {

        val labels = arrayOf("Rotavator","Harrow")
        val pies = arrayListOf<PieEntry>()


        for((i,value) in values.withIndex()){
            val ent = PieEntry((value?:0).toFloat(),labels[i])
            pies.add(ent)

        }

        val dataset = PieDataSet(pies,"Total Time")
        dataset.colors = ColorTemplate.PASTEL_COLORS.toList()
        val tv = TypedValue()
        dataset.apply {

            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            valueTextSize = 17f

            dataset.setValueFormatter { value, _, _, _ ->
                val pair = Utils.formateDate(value.toInt())
                context.getString(R.string.pie_rv_label,pair.first.toInt(),pair.second.toInt())
            }
            context.theme.resolveAttribute(R.attr.barChartLabelColor, tv,true)
            dataset.valueTextColor = tv.data

        }
        val pData = PieData(dataset)
        pieChart.apply {
            data = pData


            context.theme.resolveAttribute(R.attr.pieHoleColor, tv,true)
            setHoleColor(tv.data)
            setExtraOffsets(35f,20f,35f,20f)
            setEntryLabelTextSize(20f)
            setDrawEntryLabels(false)
            legend.isEnabled = false
            description.isEnabled = false
            animateXY(2000,2000)
            invalidate()


        }

    }



}