package com.mtu.ceit.hhk.traget.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mtu.ceit.hhk.traget.data.NightPrefImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor (
    private val nightPrefImpl: NightPrefImpl
        ) :ViewModel() {

            val nightFlow = nightPrefImpl.isNightMode().asLiveData()

         fun setNightMode(isNight:Boolean) {
           viewModelScope.launch {
               nightPrefImpl.setNightMode(isNight)
           }
        }

}