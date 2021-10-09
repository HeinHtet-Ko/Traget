package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mtu.ceit.hhk.traget.data.model.Appliance
import kotlinx.coroutines.flow.Flow
@Dao
interface ApplianceDAO {

    @Query("select * from appliance_table")
     fun getAppliances(): Flow<List<Appliance>>

    @Insert
    suspend fun insertAppliance(app:Appliance)

    @Query("select sum(price) as total from appliance_table")
    suspend fun getTotalApp():Int


}