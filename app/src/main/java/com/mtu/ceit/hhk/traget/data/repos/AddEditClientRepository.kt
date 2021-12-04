package com.mtu.ceit.hhk.traget.data.repos

import com.mtu.ceit.hhk.traget.data.local.datastore.PricePref
import com.mtu.ceit.hhk.traget.data.local.dao.ClientDAO
import com.mtu.ceit.hhk.traget.data.local.dao.DieselDAO
import com.mtu.ceit.hhk.traget.data.model.Client
import javax.inject.Inject


class AddEditClientRepository @Inject constructor(private val dieselDAO: DieselDAO, private val clientDAO: ClientDAO, private val pricePref: PricePref) {

    suspend fun getActive() = dieselDAO.getActive()

    suspend fun addClient(client:Client) = clientDAO.insertClient(client)

    suspend fun updateClient(client: Client) {
        clientDAO.updateClient(client)
    }

    fun getHrPrice() = pricePref.getHRPrice()

    fun getRvPrice() = pricePref.getRVPrice()

}