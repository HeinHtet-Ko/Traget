package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.HidePrefs
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.localdb.ClientDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

 class ClientRepository @Inject constructor(private val dao: ClientDAO,private val hidePrefs:HidePrefs) {


     suspend fun hidePaid(isHide:Boolean) {
         hidePrefs.hidePaid(isHide)
     }

     suspend fun hideUnPaid(isHide: Boolean){
         hidePrefs.hideUnPaid(isHide)
     }

     fun isHidePaid() = hidePrefs.isHidePaid()

     fun isHideUnPaid() = hidePrefs.isHideUnPaid()

     suspend fun deleteClient(client: Client) {
         dao.deleteClients(client)
     }

     suspend fun changePayStatus(status:UPDATE_PAY,id:Int) {

         when(status) {
             UPDATE_PAY.ToPaid -> dao.updateToPaid(id)
             UPDATE_PAY.ToUnPaid -> dao.updateToUnPaid(id)
         }

     }

     fun getSearch(query:String,sort: SORT,displayStatus: DISPLAY_STATUS):Flow<List<Client>> = dao.getSearch(query,sort,displayStatus)



 }

enum class UPDATE_PAY{
    ToPaid,ToUnPaid
}
enum class SORT {
    SortByAmtDesc,SortByAmtAsc,SortByDate
}
enum class DISPLAY_STATUS {
    HIDE_PAID,HIDE_UNPAID,SHOW_ALL
}
