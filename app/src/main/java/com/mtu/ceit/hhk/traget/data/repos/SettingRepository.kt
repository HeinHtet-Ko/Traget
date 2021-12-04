package com.mtu.ceit.hhk.traget.data.repos

import com.mtu.ceit.hhk.traget.data.local.datastore.NightPref
import com.mtu.ceit.hhk.traget.data.local.datastore.PricePref
import com.mtu.ceit.hhk.traget.data.local.dao.ClientDAO
import com.mtu.ceit.hhk.traget.data.local.dao.DieselDAO
import com.mtu.ceit.hhk.traget.data.local.dao.MaintenanceDAO
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val pricePref: PricePref, private val nightPref: NightPref,
    private val clientDAO: ClientDAO, private val maintainDAO: MaintenanceDAO, private val dieselDAO: DieselDAO
) {


    suspend fun setHRPrice(price:Int) = pricePref.setHRPrice(price)

    suspend fun setRVPrice(price:Int) = pricePref.setRVPrice(price)

    suspend fun setNightMode(isNight:Boolean) = nightPref.setNightMode(isNight)

    fun getHRPrice() = pricePref.getHRPrice()

    fun getRVPrice() = pricePref.getRVPrice()

    fun isNightMode() = nightPref.isNightMode()

    fun getAllClients(): Flow<List<Client>> {
        Timber.tag("fafteo").e(Thread.currentThread().name)
        return clientDAO.getAllClients()
    }

    fun getAllMaintains() = maintainDAO.getMaintains()

    fun getAllDiesels() = dieselDAO.getAllDiesel()

    suspend fun insertClient(client:Client) {
        clientDAO.insertClient(client)
    }

    suspend fun insertDiesel(diesel:Diesel) {
        dieselDAO.insertDiesel(diesel)
    }

    suspend fun insertMaintain(maintain:Maintenance){
        maintainDAO.insertMaintain(maintain)
    }

    suspend fun setActiveId(id:Int) = dieselDAO.setActive(id)

    suspend fun setAllInactive() = dieselDAO.setAllInActive()




}