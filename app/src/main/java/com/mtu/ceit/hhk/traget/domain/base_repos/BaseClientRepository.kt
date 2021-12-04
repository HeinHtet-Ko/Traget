package com.mtu.ceit.hhk.traget.domain.base_repos

import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.repos.DISPLAY_STATUS
import com.mtu.ceit.hhk.traget.data.repos.SORT_STATUS
import kotlinx.coroutines.flow.Flow

interface BaseClientRepository {

    /*
    Home
     */
    suspend fun getUnpaidSum():Int

    suspend fun getPaidSum():Int

    suspend fun getTotalRo():Int

    suspend fun getTotalHr():Int


    /*
     Clients
     */

    fun getAllClients(): Flow<List<Client>>

    suspend fun deleteClient(client: Client)

    suspend fun insertClient(client: Client)

    suspend fun updateClient(client: Client)

    suspend fun getSearchClients(query:String, sort: SORT_STATUS, displayStatus: DISPLAY_STATUS):Flow<List<Client>>

    fun getDisplayStatus():Flow<DISPLAY_STATUS>

    suspend fun setDisplayStatus(status: DISPLAY_STATUS)

}