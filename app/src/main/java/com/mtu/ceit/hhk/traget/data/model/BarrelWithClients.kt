package com.mtu.ceit.hhk.traget.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class BarrelWithClients (
        @Embedded
        val barrel:Barrel,
        @Relation
        (
                parentColumn = "bId",
                entityColumn = "barrelId"
                )
        val clientList:List<Client>

        )

data class BarrelClients (val barrel:Barrel,val clients:List<Client>)
