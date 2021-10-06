package com.mtu.ceit.hhk.traget.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mtu.ceit.hhk.traget.data.Client
import com.mtu.ceit.hhk.traget.databinding.ItemClientBinding

class ClientAdapter: ListAdapter<Client, ClientAdapter.ClientViewHolder>(ClientDifferentiator()) {


    class ClientViewHolder(private val binding: ItemClientBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(client:Client) {

            binding.apply {
                nameText.text = client.name
                val hour = client.timeTaken/60
                val min = client.timeTaken%60
               timeText.text = "$hour : $min "
                amtText.text= client.amount.toString()
                typeText.text = client.macType

                amtText.paint.isStrikeThruText = client.isPaid


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