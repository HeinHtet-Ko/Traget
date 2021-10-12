package com.mtu.ceit.hhk.traget.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface NightPref {
    fun isNightMode(): Flow<Boolean>

    suspend fun setNightMode(isNight:Boolean)
}

@Singleton
class NightPrefImpl @Inject constructor(@ApplicationContext context:Context ) {


        val ds = context.createDataStore(name = "isNightStore")


     fun isNightMode(): Flow<Boolean> = ds.data.catch { exception ->
        if(exception is IOException)
            emit(emptyPreferences())
        else
            throw exception

    }
        .map { pref ->
        pref[prefKey] ?: false
    }

     suspend fun setNightMode(isNight: Boolean) {

        ds.edit {
            it[prefKey] = isNight
        }

    }

    companion object {
        val prefKey = preferencesKey<Boolean>("isNightMode")
    }

}