package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import kotlinx.coroutines.flow.Flow
@Dao
interface MaintenanceDAO {

    @Query("select * from maintenance_table")
     fun getMaintains(): Flow<List<Maintenance>>

     @Update
     suspend fun updateMaintain(maintenance: Maintenance)

    @Insert
    suspend fun insertMaintain(app:Maintenance)

    @Query("select sum(price) as total from maintenance_table")
    suspend fun getTotalMaintainCost():Int




}