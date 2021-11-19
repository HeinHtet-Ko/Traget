package com.mtu.ceit.hhk.traget.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "diesel_table")
@Parcelize
data class Diesel (
        @PrimaryKey (autoGenerate = true)
        val bId:Int = 0,
        val price:Int,
        val isActive:Boolean = false,
        val date:Long = System.currentTimeMillis()) :Parcelable{

               // val dateFormatted = DateFormat.getDateTimeInstance().format(date)

}