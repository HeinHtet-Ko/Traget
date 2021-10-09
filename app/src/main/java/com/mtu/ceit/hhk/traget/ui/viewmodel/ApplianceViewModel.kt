package com.mtu.ceit.hhk.traget.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mtu.ceit.hhk.traget.data.model.Appliance
import com.mtu.ceit.hhk.traget.repos.ApplianceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplianceViewModel @Inject constructor(private val repository: ApplianceRepository):ViewModel() {

    lateinit var appliances:LiveData<List<Appliance>>


    fun getApp() {
        viewModelScope.launch {
            appliances = repository.getAppliances().asLiveData()
        }
    }

}