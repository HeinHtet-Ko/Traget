package com.mtu.ceit.hhk.traget.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mtu.ceit.hhk.traget.data.model.Appliance
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.model.Barrel
import com.mtu.ceit.hhk.traget.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(version = 4,entities = [Client::class,Barrel::class,Appliance::class],exportSchema = false)
abstract class ClientDB :RoomDatabase(){

    abstract fun getDAO(): ClientDAO

    abstract fun getBarrelDAO():BarrelDAO

    abstract fun getAppDAO():ApplianceDAO


     class CallBack @Inject constructor(
             private val database:Provider<ClientDB>, @ApplicationScope private val scope: CoroutineScope
    ):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val DB = database.get()
            val dao = DB.getDAO()
            val barDao = DB.getBarrelDAO()
            val appDao = DB.getAppDAO()
            scope.launch {

                barDao.insertBarrels(Barrel(1,price = 350000))
                barDao.insertBarrels(Barrel(2,price = 250000))

                appDao.insertAppliance(Appliance(name = "CrossBar",price = 8000))
                appDao.insertAppliance(Appliance(name = "Battery",price = 150000))

                dao.insertClient(Client(barrelId = 1,name = "A",timeTaken = 90,amount = 30000,isPaid = false,macType = "D"))
                dao.insertClient(Client(barrelId = 1,name = "B",timeTaken = 30,amount = 15000,isPaid = true,macType = "L"))
                dao.insertClient(Client(barrelId = 2,name = "C",timeTaken = 120,amount = 60000,isPaid = true,macType = "L"))

            }

        }
    }

}