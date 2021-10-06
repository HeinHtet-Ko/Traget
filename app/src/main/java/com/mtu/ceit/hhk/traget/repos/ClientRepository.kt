package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.Client
import com.mtu.ceit.hhk.traget.data.ClientDAO
import com.mtu.ceit.hhk.traget.data.PaidSum
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

 class ClientRepository @Inject constructor(private val dao: ClientDAO) {


      fun getAllClients():Flow<List<Client>> = dao.getAllClients()

     suspend fun insertClient(client: Client) {
         dao.insertClient(client)
     }

     suspend fun getPaidSum():PaidSum =
         dao.getPaidSum()

}