package com.mtu.ceit.hhk.traget.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.utils.ColorTemplate
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.databinding.DieselItemBinding

class DieselAdapter(): ListAdapter<Diesel, DieselAdapter.DieselViewHolder>(DieselDifferentiator()) {

    lateinit var longClick:(Diesel) -> Unit
    lateinit var click:(Diesel) -> Unit
   inner class DieselViewHolder(private val binding: DieselItemBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnLongClickListener {
                longClick.invoke(getItem(adapterPosition))
                return@setOnLongClickListener true
            }
            binding.root.setOnClickListener {
                click.invoke(getItem(adapterPosition))
            }
        }
        fun bind(bc:Diesel){
            binding.apply {
                dieselItemNoTv.text = (adapterPosition+1).toString()
                dieselItemPriceTv.text = "${bc.price} Ks"
            }

//            if(bc.isActive)
//            binding.dieselItemCard.alpha = 1f
//            else
//               binding.dieselItemCard.alpha= 0.3f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieselViewHolder {

        val binding = DieselItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return DieselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DieselViewHolder, position: Int) {

        holder.bind(getItem(position))
    }
}

    class DieselDifferentiator: DiffUtil.ItemCallback<Diesel>() {


    override fun areItemsTheSame(oldItem: Diesel, newItem: Diesel): Boolean {
       return oldItem.bId == newItem.bId
    }

    override fun areContentsTheSame(oldItem: Diesel, newItem: Diesel): Boolean {
        return oldItem == newItem
    }

}