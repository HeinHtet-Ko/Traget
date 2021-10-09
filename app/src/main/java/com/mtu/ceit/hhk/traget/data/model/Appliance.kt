package com.mtu.ceit.hhk.traget.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appliance_table")
data class Appliance(
        @PrimaryKey(autoGenerate = true)
        val id:Int =0 ,
        val name:String,
        val price:Int
)