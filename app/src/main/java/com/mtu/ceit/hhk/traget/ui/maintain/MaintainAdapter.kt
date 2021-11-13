package com.mtu.ceit.hhk.traget.ui.maintain


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import com.mtu.ceit.hhk.traget.databinding.AppItemBinding

class MaintainAdapter: ListAdapter<Maintenance, MaintainAdapter.MaintainViewHolder>(MaintainDifferentiator()) {

    lateinit var itemClickListen:(Maintenance) -> (Unit)
    lateinit var itemLongClickListen:(Maintenance) -> (Unit)

    inner class MaintainViewHolder(private val binding: AppItemBinding):RecyclerView.ViewHolder(binding.root){


        init {
            binding.root.setOnClickListener {
                itemClickListen.invoke(getItem(adapterPosition))
            }
            binding.root.setOnLongClickListener {

                itemLongClickListen.invoke(getItem(adapterPosition))
                return@setOnLongClickListener true
            }
        }

        fun bind(app:Maintenance){
            binding.apply {

                val res = root.resources

                val noStr = res.getString(R.string.maintain_item_no_str,(adapterPosition+1))
                appItemNoTv.text = noStr

                appItemNameTv.text = app.name

                val priceStr = res.getString(R.string.maintain_price_str,app.price)
                appItemPriceTv.text = priceStr
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintainViewHolder {
        val binding = AppItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MaintainViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MaintainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class MaintainDifferentiator: DiffUtil.ItemCallback<Maintenance>() {
    override fun areItemsTheSame(oldItem: Maintenance, newItem: Maintenance): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Maintenance, newItem: Maintenance): Boolean {
       return oldItem == newItem
    }

}