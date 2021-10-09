package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.localdb.ApplianceDAO
import com.mtu.ceit.hhk.traget.data.localdb.BarrelDAO
import com.mtu.ceit.hhk.traget.data.localdb.ClientDAO
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepository @Inject constructor(private val dao:ClientDAO,private val barrelDAO: BarrelDAO,private val appDAO:ApplianceDAO) {

    suspend fun getUnPaidSum() = flow {
        emit(dao.getPaidSum())
    }


    suspend fun getPaidSum() = flow {
        emit(dao.getUnPaidSum())
    }

    suspend fun getBarrelTotals() = flow {
        emit(barrelDAO.getBarrelSum())
    }

    suspend fun getTotalDollar() = flow {
        emit(dao.getTotalD())
    }

    suspend fun getTotalLinPan() = flow {
        emit(dao.getTotalL())
    }

    suspend fun getTotalMaintenance() = flow {
        emit(appDAO.getTotalApp())
    }





}