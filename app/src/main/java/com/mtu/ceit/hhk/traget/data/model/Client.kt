package com.mtu.ceit.hhk.traget.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "client_table",foreignKeys = [ForeignKey(entity = Diesel::class,
        childColumns = arrayOf("barrelId"),
        parentColumns = arrayOf("bId"),
        onDelete = CASCADE
)])
@Parcelize
data class Client(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val barrelId:Int,
        val name:String,
        val timeTaken:Int,
        val amount:Int,
        val isPaid:Boolean = false,
        val macType:String,
        val note:String? = null,
        val date:Long = System.currentTimeMillis()
):Parcelable{
      //  val dateFormatted: String? = DateFormat.getDateTimeInstance().format(date)
}

