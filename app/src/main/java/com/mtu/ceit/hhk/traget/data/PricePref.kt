package com.mtu.ceit.hhk.traget.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PricePref @Inject constructor(@ApplicationContext context: Context) {

    private val ds = context.createDataStore(
            name = "price_store"
    )

    fun getHRPrice():Flow<Int> =
        ds.data.catch {
            exception ->
            if(exception is IOException)
                emit(emptyPreferences())
            else
                throw exception

        }.map {
            it[HR_KEY] ?: 15000
        }

    fun getRVPrice():Flow<Int> =
            ds.data.catch {
                exception ->
                if(exception is IOException)
                    emit(emptyPreferences())
                else
                    throw exception

            }.map {
                it[RV_KEY] ?: 30000
            }

    suspend fun setHRPrice(price:Int) {
        ds.edit {
            it[HR_KEY] = price
        }
    }

    suspend fun setRVPrice(price:Int) {
        ds.edit {
            it[RV_KEY] = price
        }
    }

    companion object {

        val HR_KEY = preferencesKey<Int>("harrow_key")
        val RV_KEY = preferencesKey<Int>("rotavator_key")

    }


}