package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.localdb.ClientDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

 class ClientRepository @Inject constructor(private val dao: ClientDAO) {


      fun getAllClients():Flow<List<Client>> = dao.getAllClients()

     suspend fun insertClient(client: Client) {
         dao.insertClient(client)
     }

     suspend fun deleteClient(client: Client) {
         dao.deleteClients(client)
     }

     suspend fun changePayStatus(status:UPDATE_PAY,id:Int) {

         when(status) {
             UPDATE_PAY.ToPaid -> dao.upDatetoPaid(id)
             UPDATE_PAY.ToUnPaid -> dao.upDatetoUnPaid(id)
         }

     }


 }

enum class UPDATE_PAY{
    ToPaid,ToUnPaid
}