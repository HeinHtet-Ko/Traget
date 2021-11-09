package com.mtu.ceit.hhk.traget.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_table")
data class Maintenance(
        @PrimaryKey(autoGenerate = true)
        val id:Int =0 ,
        val name:String,
        val price:Int,
        val date:Long = System.currentTimeMillis()
)