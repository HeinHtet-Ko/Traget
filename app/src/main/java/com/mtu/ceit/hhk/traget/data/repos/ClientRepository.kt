package com.mtu.ceit.hhk.traget.data.repos

import com.mtu.ceit.hhk.traget.data.local.datastore.DisplayStatusPrefs
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.local.dao.ClientDAO
import com.mtu.ceit.hhk.traget.domain.base_repos.BaseClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

 class ClientRepository @Inject constructor(private val dao: ClientDAO
 , private val displayStatusPrefs: DisplayStatusPrefs):BaseClientRepository {





     override suspend fun getUnpaidSum(): Int {
        return dao.getUnPaidSum()
     }

     override suspend fun getPaidSum(): Int {
        return dao.getPaidSum()
     }

     override suspend fun getTotalRo(): Int {
        return dao.getTotalRo()
     }

     override suspend fun getTotalHr(): Int {
       return dao.getTotalHr()
     }

     override fun getAllClients(): Flow<List<Client>> {
         TODO("Not yet implemented")
     }

     override suspend fun deleteClient(client: Client) {
         dao.deleteClients(client)
     }

     suspend fun changePayStatus(status: PAY_STATUS, id:Int) {

         when(status) {
             PAY_STATUS.ToPaid -> dao.updateToPaid(id)
             PAY_STATUS.ToUnPaid -> dao.updateToUnPaid(id)
         }

     }

     override suspend fun insertClient(client: Client){
         dao.insertClient(client)
     }

     override suspend fun updateClient(client: Client) {
         TODO("Not yet implemented")
     }

     override suspend fun getSearchClients(
         query: String,
         sort: SORT_STATUS,
         displayStatus: DISPLAY_STATUS
     ): Flow<List<Client>> {
         return dao.getSearch(query,sort,displayStatus)
     }

     override fun getDisplayStatus(): Flow<DISPLAY_STATUS> {
        return displayStatusPrefs.displayStatus().map {
            return@map DISPLAY_STATUS.valueOf(it)
         }
     }

     override suspend fun setDisplayStatus(status: DISPLAY_STATUS) {
         displayStatusPrefs.editDisplayStatus(status)
     }


     //  fun getSearch(query:String, sort: SORT_STATUS, displayStatus: DISPLAY_STATUS):Flow<List<Client>> = dao.getSearch(query,sort,displayStatus)



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
