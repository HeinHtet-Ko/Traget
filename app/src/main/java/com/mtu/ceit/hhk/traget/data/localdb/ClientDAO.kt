package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mtu.ceit.hhk.traget.data.model.Client

import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDAO {


    @Query("select * from client_table")
     fun getAllClients(): Flow<List<Client>>

     @Delete
     suspend fun deleteClients(client: Client)

    @Insert
    suspend fun insertClient(client: Client)

    @Query("select sum(amount) as total from client_table where isPaid = 0 ")
    suspend fun getUnPaidSum(): Int

    @Query("select sum(amount) as total from client_table where isPaid = 1 ")
    suspend fun getPaidSum():Int

    @Query("select sum(timeTaken) as total from client_table where macType = 'D' ")
    suspend fun getTotalD():Int

    @Query("select sum(timeTaken) as total from client_table where macType = 'L' ")
    suspend fun getTotalL():Int

    @Query("update client_table set isPaid = 1 where id = :cId ")
    suspend fun upDatetoPaid(cId:Int)

    @Query("update client_table set isPaid = 0 where id = :cId ")
    suspend fun upDatetoUnPaid(cId:Int)




}

