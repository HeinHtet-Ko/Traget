package com.mtu.ceit.hhk.traget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client_table")
data class Client(
        @PrimaryKey(autoGenerate = true)

        val id: Int = 0,
        val name:String,
        val timeTaken:Int,
        val amount:Int,
        val isPaid:Boolean = false,
        val macType:String,
        val date:Long = System.currentTimeMillis()
)

data class PaidSum(
        val total:Int
)