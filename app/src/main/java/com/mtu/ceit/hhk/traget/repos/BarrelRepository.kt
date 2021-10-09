package com.mtu.ceit.hhk.traget.repos

import androidx.annotation.WorkerThread
import com.mtu.ceit.hhk.traget.data.localdb.BarrelDAO
import com.mtu.ceit.hhk.traget.data.model.Barrel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class BarrelRepository @Inject constructor(private val dao:BarrelDAO) {

    suspend fun insertBarrel(barrel:Barrel) {

        dao.insertBarrels(barrel)


    }


     fun getAllBarrels() = dao.getAllBarrels()

    fun getBarrelsWithClients() =
        dao.getBarrelsWithClients()


}