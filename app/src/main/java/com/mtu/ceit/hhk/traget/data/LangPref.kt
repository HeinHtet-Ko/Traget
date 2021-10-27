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
class LangPref @Inject constructor(@ApplicationContext context:Context) {

    val ds = context.createDataStore(name = "language_store")


    fun currentLang(): Flow<String> = ds.data
            .catch { e ->
                if(e is IOException)
                    emit(emptyPreferences())
                else
                    throw e

            }
            .map {
        it[languageKey] ?: "en"
    }

    suspend fun setCurrentLang(lang:String) {
        ds.edit {
            it[languageKey] = lang
        }
    }

    companion object {
        val languageKey = preferencesKey<String>("language_key")
    }

}