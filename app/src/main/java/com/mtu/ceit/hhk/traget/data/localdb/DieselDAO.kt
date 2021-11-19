package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.*
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.data.model.DieselWithClients
import kotlinx.coroutines.flow.Flow

@Dao
interface DieselDAO {

    @Query("select * from diesel_table")
    fun getAllDiesel(): Flow<List<Diesel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiesel(diesel: Diesel)

    @Transaction
    @Query("select * from diesel_table")
    fun getDieselWithClients():List<DieselWithClients>

    @Query("select sum(price) as price from diesel_table")
    suspend fun getDieselSum():Int

    @Query("update diesel_table set isActive = 1 where bId = :id")
    suspend fun setActive(id:Int)

    @Query("update diesel_table set isActive = 0 where isActive = 1 ")
    suspend fun setAllInActive()

    @Query("select bId as id from diesel_table where isActive=1")
    suspend fun getActive():Int

}

