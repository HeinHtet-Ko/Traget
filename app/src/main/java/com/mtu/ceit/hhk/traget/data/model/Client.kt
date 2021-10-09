package com.mtu.ceit.hhk.traget.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.mtu.ceit.hhk.traget.data.model.Barrel

@Entity(tableName = "client_table",foreignKeys = [ForeignKey(entity = Barrel::class,
        childColumns = arrayOf("barrelId"),
        parentColumns = arrayOf("bId"),
        onDelete = CASCADE
)])
data class Client(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val barrelId:Int,
        val name:String,
        val timeTaken:Int,
        val amount:Int,
        val isPaid:Boolean = false,
        val macType:String,
        val date:Long = System.currentTimeMillis()
)

