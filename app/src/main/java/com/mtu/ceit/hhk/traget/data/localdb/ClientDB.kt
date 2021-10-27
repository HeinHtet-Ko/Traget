package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(version = 9,entities = [Client::class,Diesel::class,Maintenance::class],exportSchema = false)
abstract class ClientDB :RoomDatabase(){

    abstract fun getDAO(): ClientDAO

    abstract fun getBarrelDAO():DieselDAO

    abstract fun getMaintainDAO():MaintenanceDAO


     class CallBack @Inject constructor(
             private val database:Provider<ClientDB>, @ApplicationScope private val scope: CoroutineScope
    ):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val DB = database.get()
            val dao = DB.getDAO()
            val barDao = DB.getBarrelDAO()
            val appDao = DB.getMaintainDAO()
            scope.launch {

                barDao.insertDiesel(Diesel(1,price = 350000))
                barDao.insertDiesel(Diesel(2,price = 250000))

                appDao.insertMaintain(Maintenance(name = "CrossBar",price = 8000))
                appDao.insertMaintain(Maintenance(name = "Battery",price = 150000))

                dao.insertClient(Client(barrelId = 1,name = "Aero",timeTaken = 90,amount = 30000,isPaid = false,macType = "Rotavator",note = "20000Paid"))
                dao.insertClient(Client(barrelId = 1,name = "Blith",timeTaken = 30,amount = 15000,isPaid = true,macType = "Harrow"))
                dao.insertClient(Client(barrelId = 2,name = "Cisco",timeTaken = 120,amount = 60000,isPaid = true,macType = "Harrow",note = "5000short"))

            }

        }
    }

}