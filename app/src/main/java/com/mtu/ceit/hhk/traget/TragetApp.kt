package com.mtu.ceit.hhk.traget

import android.app.Application

import com.mtu.ceit.hhk.traget.util.Utils
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TragetApp :Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Lingver.init(this,"en")
    }


//    override fun attachBaseContext(newBase: Context?) {
//        newBase?.let {
//            //val lp = EntryPoints.get(it.applicationContext,LangPrefInt::class.java).langPref
//            val lp = EntryPointAccessors.fromApplication(newBase.applicationContext, LangPrefInt::class.java).langPref
//            GlobalScope.launch {
//                val c = lp.currentLang().first()
//                val con = lp.changeLanguage("my")
//                con?.let { con ->
//                    super.attachBaseContext(con)
//                }
//
//
//            }
//        }
//
//    }

}