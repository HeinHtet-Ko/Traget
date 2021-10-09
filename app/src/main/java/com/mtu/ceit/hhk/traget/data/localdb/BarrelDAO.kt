package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mtu.ceit.hhk.traget.data.model.Barrel
import com.mtu.ceit.hhk.traget.data.model.BarrelWithClients
import kotlinx.coroutines.flow.Flow

@Dao
interface BarrelDAO {

    @Query("select * from barrel_table")
     fun getAllBarrels(): Flow<List<Barrel>>

    @Insert
    suspend fun insertBarrels(barrel: Barrel)

    @Transaction
    @Query("select * from barrel_table")
    fun getBarrelsWithClients():List<BarrelWithClients>

    @Query("select sum(price) as price from barrel_table")
    suspend fun getBarrelSum():Int

}

