package com.mtu.ceit.hhk.traget.ui.diesel

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.mtu.ceit.hhk.traget.util.Constants
import com.mtu.ceit.hhk.traget.util.Utils
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.text.DateFormat

class DieselWithClientAdapter: RecyclerView.Adapter<DieselWithClientViewHolder>() {


    var itemLongClick : ((Int)->Unit)? = null


    var itemList = mutableListOf<DieselWithClientModel>()


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
                        itemList = collapse(position,itemList)
                        item.isExpanded = false
                    }
                    else{
                        itemList= expand(position,itemList)
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


    private fun calculateTotal(clients: List<Client>):Triple<String,String,String>
    {

        var hr = 0
        var rv = 0
        var amt = 0

        if(!clients.isNullOrEmpty()){
              clients.forEach { client ->
                  amt += client.amount
                  if(client.macType == Constants.HARROW){
                      hr += client.timeTaken
                  }else{
                      rv += client.timeTaken
                  }
              }
        }

        return Triple(amt.toString(),rv.toString(),hr.toString())
    }

    private fun expand(position: Int, listItem:MutableList<DieselWithClientModel>):MutableList<DieselWithClientModel>
    {
        val item = listItem[position] as DieselWithClientModel.Parent_Diesel
        var nextPosition = position


                    val trip = calculateTotal(item.dieselwithCli.clientList)
                    val ls =  item.dieselwithCli.clientList.map { DieselWithClientModel.Child_Client(it) }
                    listItem.add(++nextPosition,DieselWithClientModel.Child_Total(trip))
                    listItem.addAll(++nextPosition,ls)

                    notifyItemRangeInserted((1+position),ls.size+1)


        return listItem

    }

    private fun collapse (position:Int, listItem:MutableList<DieselWithClientModel>):MutableList<DieselWithClientModel>
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


         fun bind(client: Client) {

             val res = binding.root.context.resources
             binding.apply {

                 itemClientNameTv.text = client.name
                 val pair:Pair<String,String> = Utils.formateDate(client.timeTaken)
                 val timeStr = res.getString(R.string.client_time_str,pair.first.toInt(),pair.second.toInt())
                 itemClientTimeTv.text  = timeStr

                 val amtStr = res.getString(R.string.client_amt_str,client.amount)
                 itemClientAmtTv.text= amtStr

                 val hrShortStr = res.getString(R.string.hr_short_str)
                 val rvShortStr = res.getString(R.string.rv_short_str)
                 itemClientMacTv.text = if(client.macType == Constants.HARROW) hrShortStr else rvShortStr
                 itemClientNoTv.text = res.getString(R.string.item_no,(adapterPosition+1))
                 if(!client.note.isNullOrEmpty())
                     itemClientNameTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_note,0)

                 if (client.isPaid)
                     root.background = ContextCompat.getDrawable(binding.root.context,R.drawable.paid_client)
                 else
                     root.background = ContextCompat.getDrawable(binding.root.context,R.drawable.unpaid_client)


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





