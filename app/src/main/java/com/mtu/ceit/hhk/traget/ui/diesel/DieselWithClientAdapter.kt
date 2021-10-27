package com.mtu.ceit.hhk.traget.ui.diesel

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.databinding.DieselItemBinding
import com.mtu.ceit.hhk.traget.databinding.ItemClientBinding
import com.mtu.ceit.hhk.traget.databinding.TotalPerDieselItemBinding
import com.mtu.ceit.hhk.traget.util.Utils
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.text.DateFormat

class DieselWithClientAdapter: RecyclerView.Adapter<DieselWithClientViewHolder>() {


    var itemLongClick : ((Int)->Unit)? = null


    var itemList = mutableListOf<DieselWithClientModel>()
        set(value) {
        field = value
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieselWithClientViewHolder {

        return when(viewType) {
            R.layout.diesel_item -> {
                val binding = DieselItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                DieselWithClientViewHolder.DieselParentViewHolder(binding)
            }
            R.layout.item_client -> {
                val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                DieselWithClientViewHolder.ClientChildViewHolder(binding)
            }
            R.layout.total_per_diesel_item -> {
                val binding = TotalPerDieselItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                DieselWithClientViewHolder.TotalChildViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid Viewtype ")
        }


    }

    override fun onBindViewHolder(holder: DieselWithClientViewHolder, position: Int) {

        val item = itemList[position]
        when(holder) {
            is DieselWithClientViewHolder.DieselParentViewHolder -> {


                holder.bind((item as DieselWithClientModel.Parent_Diesel).dieselwithCli.diesel)
                holder.itemLongClick = itemLongClick
                holder.binding.root.setOnClickListener {

                    val isExpanded = (item).isExpanded
                    if(isExpanded)
                    {
                        val ls = itemList
                       itemList = collapse(position,ls)
                        item.isExpanded = false
                    }else{
                        val ls = itemList
                       val pair = expand(position,ls)
                        itemList = pair.first
                        notifyItemRangeInserted(1+position,pair.second+1)
                        item.isExpanded = true
                    }



                }


            }
            is DieselWithClientViewHolder.ClientChildViewHolder -> {
                holder.bind((item as DieselWithClientModel.Child_Client).client)
            }
            is DieselWithClientViewHolder.TotalChildViewHolder -> {
                holder.bind((item as DieselWithClientModel.Child_Total).totals)
            }
        }


    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when(itemList[position]){
            is DieselWithClientModel.Child_Client -> R.layout.item_client
            is DieselWithClientModel.Parent_Diesel -> R.layout.diesel_item
            is DieselWithClientModel.Child_Total -> R.layout.total_per_diesel_item
        }
    }


    fun expand(position: Int,listItem:MutableList<DieselWithClientModel>):Pair<MutableList<DieselWithClientModel>,Int> {
        val item = listItem[position]
        var nextPosition = position
        var expandedSize = 0
        when(item) {
            is DieselWithClientModel.Parent_Diesel -> {

                var hr = 0
                var rv = 0
                var amt = 0

                if(!item.dieselwithCli.clientList.isNullOrEmpty()){
                    item.dieselwithCli.clientList.forEach { client ->

                        amt += client.amount
                        if(client.macType == Utils.HARROW){
                            hr += client.timeTaken
                        }else{
                            rv += client.timeTaken
                        }
                    }
                    val ls =  item.dieselwithCli.clientList.map { DieselWithClientModel.Child_Client(it) }
                    listItem.add(++nextPosition,DieselWithClientModel.Child_Total(Triple(amt.toString(),rv.toString(),hr.toString())))
                    listItem.addAll(++nextPosition,ls)
                    expandedSize = ls.size
                    notifyItemRangeInserted((1+position),ls.size+1)



                }else{


                }
            }
            else -> {

            }
        }

        return Pair(listItem,expandedSize)

    }

    fun collapse (position:Int,listItem:MutableList<DieselWithClientModel>):MutableList<DieselWithClientModel>
    {
        val nextPosition = position +1
        outerloop@ while(true){
            if (nextPosition == listItem.size || listItem[nextPosition] is DieselWithClientModel.Parent_Diesel){
                break@outerloop
            }
            listItem.removeAt(nextPosition)
            notifyItemRemoved(nextPosition)

        }

        return listItem

    }


}

sealed class DieselWithClientViewHolder(binding:ViewBinding):RecyclerView.ViewHolder(binding.root){

    class DieselParentViewHolder(val binding:DieselItemBinding):DieselWithClientViewHolder(binding){

        var itemLongClick : ((Int)->Unit)? = null




        fun bind(diesel: Diesel){



            binding.apply {
                dieselItemPriceTv.text = "${diesel.price} Ks"
                dieselItemNoTv.text = (adapterPosition+1).toString()
                dieselItemDateTv.text = DateFormat.getDateInstance().format(diesel.date)
                if(diesel.isActive)
                    root.alpha = 1f
                else
                    root.alpha = 0.3f

                root.setOnLongClickListener {

                    itemLongClick?.invoke(diesel.bId)
                    return@setOnLongClickListener true
                }
            }

        }
    }

     class ClientChildViewHolder(val binding:ItemClientBinding):DieselWithClientViewHolder(binding){
        fun bind(client:Client){

            binding.apply {
                itemClientNoTv.text = ""
                itemClientNameTv.text = client.name
                itemClientMacTv.text = client.macType
                itemClientTimeTv.text = client.timeTaken.toString()
                itemClientAmtTv.text = client.amount.toString()

               val color =  if(client.isPaid) 0 else 1

                root.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[color])

            }
        }
    }

    class TotalChildViewHolder(val binding:TotalPerDieselItemBinding):DieselWithClientViewHolder(binding){
        fun bind(totals:Triple<String,String,String>){
            binding.apply {
                totPerDsAmtTv.text = ("${totals.first} Ks")
                totPerDsRvTv.text = ("${totals.second} min")
                totPerDsHrTv.text = ("${totals.third} min")
            }
        }
    }
}





