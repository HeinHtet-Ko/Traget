package com.mtu.ceit.hhk.traget.data

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LangPref @Inject constructor(@ApplicationContext val context:Context) {

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


    fun changeLanguage(lang:String): Context? {

        var newContext:Context?= null
        var sysLocale: Locale?
        val rs = context.resources
        val config = Configuration(rs.configuration)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            sysLocale = config.locales[0]
        }else{
            sysLocale = config.locale
        }
        if(lang != "" && sysLocale.language != lang){
            val locale = Locale(lang)
            Locale.setDefault(locale)
            config.setLocale(locale)

            newContext = context.createConfigurationContext(config)
        }

        return newContext



    }


    companion object {
        val languageKey = preferencesKey<String>("language_key")
    }

}