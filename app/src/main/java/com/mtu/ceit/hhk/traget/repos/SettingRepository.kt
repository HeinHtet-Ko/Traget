package com.mtu.ceit.hhk.traget.repos

import com.mtu.ceit.hhk.traget.data.LangPref
import com.mtu.ceit.hhk.traget.data.NightPref
import com.mtu.ceit.hhk.traget.data.PricePref
import javax.inject.Inject

class SettingRepository @Inject constructor(private val pricePref: PricePref, private val nightPref: NightPref
,private val langPref: LangPref) {


    suspend fun setHRPrice(price:Int) = pricePref.setHRPrice(price)

    suspend fun setRVPrice(price:Int) = pricePref.setRVPrice(price)

    suspend fun setNightMode(isNight:Boolean) = nightPref.setNightMode(isNight)

    suspend fun setLang(lang:String) = langPref.setCurrentLang(lang)

    fun getHRPrice() = pricePref.getHRPrice()

    fun getRVPrice() = pricePref.getRVPrice()

    fun isNightMode() = nightPref.isNightMode()

    fun currentLang() = langPref.currentLang()



}