package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.*
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.repos.DISPLAY_STATUS
import com.mtu.ceit.hhk.traget.repos.SORT_STATUS

import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDAO {


    @Query("select * from client_table")
    fun getAllClients(): Flow<List<Client>>

    @Delete
    suspend fun deleteClients(client: Client)

    @Insert
    suspend fun insertClient(client: Client)

    @Update
    suspend fun updateClient(client:Client)

    @Query("select sum(amount) as total from client_table where isPaid = 0 ")
    suspend fun getUnPaidSum(): Int

    @Query("select sum(amount) as total from client_table where isPaid = 1 ")
    suspend fun getPaidSum():Int

    @Query("select sum(timeTaken) as total from client_table where macType = 'Rotavator' ")
    suspend fun getTotalRo():Int

    @Query("select sum(timeTaken) as total from client_table where macType = 'Rotavator' and barrelId = :bId " )
    suspend fun getRvSumPerBarrel(bId:Int):Int

    @Query("select sum(timeTaken) as total from client_table where macType = 'Harrow' ")
    suspend fun getTotalHr():Int

    @Query("update client_table set isPaid = 1 where id = :cId ")
    suspend fun updateToPaid(cId:Int)

    @Query("update client_table set isPaid = 0 where id = :cId ")
    suspend fun updateToUnPaid(cId:Int)

    @Query("select * from client_table where name like '%' || :query || '%'  order by amount desc")
     fun getSortByAmtDesc(query: String):Flow<List<Client>>

    @Query("select * from client_table where isPaid =:isPaid and name like '%' || :query || '%'  order by amount desc")
    fun getSortByAmtDescWithDisplay(query: String,isPaid: Int):Flow<List<Client>>

    @Query("select * from client_table where name like '%' || :query || '%'  order by amount asc")
     fun getSortByAmtAsc(query: String):Flow<List<Client>>

    @Query("select * from client_table where isPaid =:isPaid and name like '%' || :query || '%'  order by amount asc")
    fun getSortByAmtAscWithDisplay(query: String,isPaid: Int):Flow<List<Client>>

    @Query("select * from client_table where isPaid = :isPaid and name like '%' || :query || '%'  order by date asc")
     fun getSortByDateWithDisplay(query: String,isPaid:Int):Flow<List<Client>>

    @Query("select * from client_table where name like '%' || :query || '%'  order by date asc")
    fun getSortByDate(query: String):Flow<List<Client>>


     fun getSearch(query:String,sort: SORT_STATUS,displayStatus: DISPLAY_STATUS):Flow<List<Client>>
     =
         when(sort) {
             SORT_STATUS.SortByDate -> {
                 when(displayStatus) {
                     DISPLAY_STATUS.HIDE_PAID -> getSortByDateWithDisplay(query,0)
                     DISPLAY_STATUS.HIDE_UNPAID -> getSortByDateWithDisplay(query,1)
                     DISPLAY_STATUS.SHOW_ALL -> getSortByDate(query)
                 }

             }
             SORT_STATUS.SortByAmtAsc -> {

                 when(displayStatus) {
                     DISPLAY_STATUS.HIDE_PAID -> getSortByAmtAscWithDisplay(query,0)
                     DISPLAY_STATUS.HIDE_UNPAID -> getSortByAmtAscWithDisplay(query,1)
                     DISPLAY_STATUS.SHOW_ALL -> getSortByAmtAsc(query)
                 }

             }
             SORT_STATUS.SortByAmtDesc -> {
                 when(displayStatus) {
                     DISPLAY_STATUS.HIDE_PAID -> getSortByAmtDescWithDisplay(query,0)
                     DISPLAY_STATUS.HIDE_UNPAID -> getSortByAmtDescWithDisplay(query,1)
                     DISPLAY_STATUS.SHOW_ALL -> getSortByAmtDesc(query)
                 }

             }
         }






}

