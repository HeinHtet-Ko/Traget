package com.mtu.ceit.hhk.traget.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.mtu.ceit.hhk.traget.data.repos.DISPLAY_STATUS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DisplayStatusPrefs @Inject constructor(@ApplicationContext context: Context){

    private val ds = context.createDataStore( name = "display_store")

    fun displayStatus():Flow<String> = ds.data
            .catch {
                e ->
                if(e is IOException)
                    emit(emptyPreferences())
                else
                    throw e

            }
            .map {
        it[displayStatusKey] ?: DISPLAY_STATUS.SHOW_ALL.name
    }

    suspend fun editDisplayStatus(status: DISPLAY_STATUS) = ds.edit {
        it[displayStatusKey] = status.name
    }


    companion object {
        val displayStatusKey = preferencesKey<String>("display_status_key")
    }

}