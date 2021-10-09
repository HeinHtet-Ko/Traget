package com.mtu.ceit.hhk.traget.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mtu.ceit.hhk.traget.data.model.Appliance
import com.mtu.ceit.hhk.traget.databinding.AppItemBinding

class AppAdapter: ListAdapter<Appliance, AppAdapter.AppViewHolder>(AppDifferentiator()) {


    inner class AppViewHolder(private val binding: AppItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(app:Appliance){
            binding.apply {
                appId.text = app.id.toString()
                appName.text = app.name
                appPrice.text = app.price.toString()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = AppItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return AppViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AppDifferentiator: DiffUtil.ItemCallback<Appliance>() {
    override fun areItemsTheSame(oldItem: Appliance, newItem: Appliance): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Appliance, newItem: Appliance): Boolean {
       return oldItem == newItem
    }

}