package com.mtu.ceit.hhk.traget.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.color.MaterialColors
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.databinding.ItemClientBinding
import com.mtu.ceit.hhk.traget.util.Constants

class ClientAdapter: ListAdapter<Client, ClientAdapter.ClientViewHolder>(ClientDifferentiator()) {

    lateinit var longClick:(Client) -> Unit

    lateinit var click:(Client) -> Unit

    inner class ClientViewHolder(private val binding: ItemClientBinding):RecyclerView.ViewHolder(binding.root){

        init {


           binding.root.setOnLongClickListener {
               longClick(getItem(adapterPosition))
               return@setOnLongClickListener true
           }

            binding.root.setOnClickListener {
                click(getItem(adapterPosition))

            }
        }



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
                    itemClientAmtTv.setTextColor(ColorTemplate.MATERIAL_COLORS[0])
                else
                    itemClientAmtTv.setTextColor(ColorTemplate.MATERIAL_COLORS[1])


            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {

        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ClientDifferentiator: DiffUtil.ItemCallback<Client>() {
    override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem ==  newItem
    }

}