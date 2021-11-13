package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.localdb.MaintenanceDAO
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import javax.inject.Inject


class MaintainRepository @Inject constructor(private val dao:MaintenanceDAO) {


     fun getMaintains() =
        dao.getMaintains()

   suspend fun insertMaintenance(maintain:Maintenance) {
        dao.insertMaintain(maintain)
    }

    suspend fun updateMaintain(maintain: Maintenance) {
        dao.updateMaintain(maintain)
    }

    suspend fun deleteMaintain(maintain: Maintenance) {
        dao.deleteMaintain(maintain)
    }

}