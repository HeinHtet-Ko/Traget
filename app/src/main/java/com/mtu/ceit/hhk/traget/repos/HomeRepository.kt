package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.localdb.MaintenanceDAO
import com.mtu.ceit.hhk.traget.data.localdb.DieselDAO
import com.mtu.ceit.hhk.traget.data.localdb.ClientDAO
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepository @Inject constructor(private val dao:ClientDAO,private val dieselDAO: DieselDAO,private val appDAO:MaintenanceDAO) {

    suspend fun getUnPaidSum() = dao.getUnPaidSum()


    suspend fun getPaidSum() = dao.getPaidSum()

    suspend fun getDieselTotal() = dieselDAO.getDieselSum()

    suspend fun getTotalMaintenance() = appDAO.getTotalMaintainCost()

    suspend fun getTotalRo() = flow {
        emit(dao.getTotalRo())
    }

    suspend fun getTotalHr() = flow {
        emit(dao.getTotalHr())
    }







}