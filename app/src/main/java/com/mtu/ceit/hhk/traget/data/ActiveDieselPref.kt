package com.mtu.ceit.hhk.traget.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.mtu.ceit.hhk.traget.data.NightPref.Companion.prefKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiveDieselPref @Inject constructor (@ApplicationContext val context: Context) {

    val ds = context.createDataStore( name = "active_diesel_store")

    fun activeDieselID():Flow<Int> = ds.data.catch { exception ->
        if(exception is IOException)
            emit(emptyPreferences())
        else
            throw exception

    }.map {  pref ->
        pref[prefKey] ?: 1

    }

  suspend fun setActive(id:Int) = ds.edit {
      it[prefKey] = id
  }

    companion object {
         val prefKey = preferencesKey<Int>("active_diesel_id")
    }
}