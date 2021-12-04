package com.mtu.ceit.hhk.traget.features.diesel

import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.model.DieselWithClients

sealed class DieselWithClientModel {


    data class Parent_Diesel(val dieselwithCli:DieselWithClients, var isExpanded:Boolean = false):DieselWithClientModel()

    data class Child_Client(val client: Client):DieselWithClientModel()

    data class Child_Total(val totals:Triple<String,String,String>):DieselWithClientModel()

}