package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.DisplayStatusPrefs
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.localdb.ClientDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

 class ClientRepository @Inject constructor(private val dao: ClientDAO,private val displayStatusPrefs: DisplayStatusPrefs) {



     fun displayStatus() = displayStatusPrefs.displayStatus()

     suspend fun setDisplayStatus(status: DISPLAY_STATUS) {
         displayStatusPrefs.editDisplayStatus(status)
     }

     suspend fun deleteClient(client: Client) {
         dao.deleteClients(client)
     }

     suspend fun changePayStatus(status:PAY_STATUS,id:Int) {

         when(status) {
             PAY_STATUS.ToPaid -> dao.updateToPaid(id)
             PAY_STATUS.ToUnPaid -> dao.updateToUnPaid(id)
         }

     }

     fun getSearch(query:String,sort: SORT_STATUS,displayStatus: DISPLAY_STATUS):Flow<List<Client>> = dao.getSearch(query,sort,displayStatus)



 }

enum class PAY_STATUS{
    ToPaid,ToUnPaid
}
enum class SORT_STATUS {
    SortByAmtDesc,SortByAmtAsc,SortByDate
}
enum class DISPLAY_STATUS {
    HIDE_PAID,HIDE_UNPAID,SHOW_ALL
}
