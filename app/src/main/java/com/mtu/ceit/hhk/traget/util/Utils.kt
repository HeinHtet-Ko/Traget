package com.mtu.ceit.hhk.traget.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import timber.log.Timber
import java.util.*

class Utils {

    companion object {



        fun changeLanguage(context:Context,lang:String): Context? {

            var newContext:Context?= null
            var sysLocale:Locale?
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


        fun formateDate(it:Int):Pair<String,String> {
            val min = it%60
            val hour = it/60

           return if (min<=9)
               Pair(hour.toString(),"0$min")
                else
               Pair(hour.toString(),"$min")
        }

    }




}