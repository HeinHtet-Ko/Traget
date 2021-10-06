package com.mtu.ceit.hhk.traget.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mtu.ceit.hhk.traget.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(version = 1,entities = [Client::class],exportSchema = false)
abstract class ClientDB :RoomDatabase(){

    abstract fun getDAO():ClientDAO


     class CallBack @Inject constructor(
       private val database:Provider<ClientDB> , @ApplicationScope private val scope: CoroutineScope
    ):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().getDAO()
            scope.launch {
                dao.insertClient(Client(name = "A",timeTaken = 90,amount = 30000,isPaid = false,macType = "D"))
                dao.insertClient(Client(name = "B",timeTaken = 30,amount = 15000,isPaid = true,macType = "D"))
                dao.insertClient(Client(name = "C",timeTaken = 120,amount = 60000,isPaid = true,macType = "D"))

            }

        }
    }

}