package com.mtu.ceit.hhk.traget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDAO {


    @Query("select * from client_table")
     fun getAllClients(): Flow<List<Client>>

    @Insert
    suspend fun insertClient(client: Client)

    @Query("select sum(amount) as total from client_table where isPaid = 0 ")
    suspend fun getPaidSum():PaidSum

}