package com.mtu.ceit.hhk.traget.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mtu.ceit.hhk.traget.data.model.Barrel
import com.mtu.ceit.hhk.traget.data.model.BarrelClients
import com.mtu.ceit.hhk.traget.data.model.BarrelWithClients
import com.mtu.ceit.hhk.traget.databinding.BarrelItemBinding

class BarrelAdapter( val lambda:(Barrel) -> Unit): ListAdapter<Barrel, BarrelAdapter.BarrelViewHolder>(BarrelDifferentiator()) {

   inner class BarrelViewHolder(private val binding: BarrelItemBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                lambda.invoke(getItem(adapterPosition))
            }
        }
        fun bind(bc:Barrel){
            binding.apply {
                barrelNo.text = bc.bId.toString()
                barrelPrice.text = bc.price.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarrelViewHolder {

        val binding = BarrelItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return BarrelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarrelViewHolder, position: Int) {

        holder.bind(getItem(position))
    }
}

class BarrelDifferentiator: DiffUtil.ItemCallback<Barrel>() {


    override fun areItemsTheSame(oldItem: Barrel, newItem: Barrel): Boolean {
       return oldItem.bId == newItem.bId
    }

    override fun areContentsTheSame(oldItem: Barrel, newItem: Barrel): Boolean {
        return oldItem == newItem
    }

}