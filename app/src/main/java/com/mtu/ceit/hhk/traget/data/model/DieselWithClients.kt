package com.mtu.ceit.hhk.traget.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class DieselWithClients (
        @Embedded
        val diesel:Diesel,
        @Relation
        (
                parentColumn = "bId",
                entityColumn = "barrelId"
                )
        val clientList:List<Client>

        )


