package com.mtu.ceit.hhk.traget.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barrel_table")
data class Barrel (
        @PrimaryKey (autoGenerate = true)
        val bId:Int = 0,val price:Int) {

}