package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.ActiveDieselPref
import com.mtu.ceit.hhk.traget.data.localdb.DieselDAO
import com.mtu.ceit.hhk.traget.data.model.Diesel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class DieselRepository @Inject constructor(private val dao:DieselDAO) {

    suspend fun insertBarrel(diesel:Diesel) {
        dao.insertDiesel(diesel)
    }

    suspend fun getActiveId() = flowOf(dao.getActive())

    suspend fun setActiveId(id:Int) = dao.setActive(id)

    suspend fun setAllInactive() = dao.setAllInActive()

    suspend fun deleteDiesel(diesel: Diesel) {
        dao.deleteDiesel(diesel)
    }


    fun getAllBarrels() = dao.getAllDiesel()

    fun getBarrelsWithClients() =
        dao.getDieselWithClients()


}