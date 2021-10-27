package com.mtu.ceit.hhk.traget.ui.setting

import android.view.View
import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.NightPref
import com.mtu.ceit.hhk.traget.repos.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor (
        private val repository: SettingRepository
        ) :ViewModel() {

    private val _bottomNavigationVisibility:MutableLiveData<Int> = MutableLiveData<Int>()
    val bottomNavigationVisibility:LiveData<Int> = _bottomNavigationVisibility


    val isNight = repository.isNightMode().asLiveData()
    val hrPrice = repository.getHRPrice().asLiveData()
    val rvPrice = repository.getRVPrice().asLiveData()
    val currentLang = repository.currentLang().asLiveData()


            fun hideBottonNavigation() {
                viewModelScope.launch {
                    _bottomNavigationVisibility.value = View.GONE
                }
            }

            fun showBottomNavigation() {
                viewModelScope.launch {
                    _bottomNavigationVisibility.value = View.VISIBLE
                }
            }



         fun setNightMode(isNight:Boolean) {
           viewModelScope.launch {
               repository.setNightMode(isNight)
           }
        }

        fun setRvPrice(price:Int) {
            viewModelScope.launch {
                repository.setRVPrice(price)
            }
        }

        fun setHrPrice(price:Int) {
            viewModelScope.launch {
                repository.setHRPrice(price)
            }
        }

        fun setLanguage(lang:String) {
            viewModelScope.launch {
                repository.setLang(lang)
            }
        }



}