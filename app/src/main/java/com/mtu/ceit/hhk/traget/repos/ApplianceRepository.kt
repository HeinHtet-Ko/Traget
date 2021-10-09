package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.localdb.ApplianceDAO
import javax.inject.Inject


class ApplianceRepository @Inject constructor(private val dao:ApplianceDAO) {


    suspend fun getAppliances() =
        dao.getAppliances()

}