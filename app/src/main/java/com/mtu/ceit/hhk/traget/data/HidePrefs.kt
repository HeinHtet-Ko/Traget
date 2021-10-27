package com.mtu.ceit.hhk.traget.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HidePrefs @Inject constructor(@ApplicationContext context: Context ) {

    private val ds = context.createDataStore( name = "hide_store")


    fun isHidePaid() = ds.data.catch { e ->
        if (e is IOException)
        emit(emptyPreferences())
        else
            throw e
    }.map {
        it[HIDE_PAID_KEY] ?: false
    }

    fun isHideUnPaid() = ds.data.catch { e ->
        if(e is IOException)
            emit(emptyPreferences())
        else
            throw e
    }.map {
        it[HIDE_UNPAID_KEY] ?: false
    }


    suspend fun hidePaid(isHide: Boolean){
        ds.edit {
            it[HIDE_PAID_KEY] = isHide
        }
    }

    suspend fun hideUnPaid(isHide: Boolean){
        ds.edit {
            it[HIDE_UNPAID_KEY] = isHide
        }
    }


    companion object {
        val HIDE_PAID_KEY = preferencesKey<Boolean>("hide_paid")
        val HIDE_UNPAID_KEY = preferencesKey<Boolean>("hide_unpaid")

    }


}