package com.mtu.ceit.hhk.traget.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.utils.ColorTemplate
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.databinding.ItemClientBinding

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

            binding.apply {
                itemClientNameTv.text = client.name
                val pair:Pair<String,String> = Utils.formateDate(client.timeTaken)
                itemClientTimeTv.text ="${pair.first}:${pair.second}"
                itemClientAmtTv.text= client.amount.toString()
                itemClientMacTv.text = client.macType
                itemClientNoTv.text = "${adapterPosition+1}."
                if(!client.note.isNullOrEmpty())
                    itemClientNameTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_note,0,0,0)

                if (client.isPaid)
                    root.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[0])
                else
                    root.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[1])


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