package com.mtu.ceit.hhk.traget.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.repos.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) :ViewModel(){


     var paidSum:LiveData<Int?> = MutableLiveData()

     var unPaidSum:LiveData<Int?> = MutableLiveData()

     var barrelSum:LiveData<Int?> = MutableLiveData()

    var maintenanceSum:LiveData<Int?> = MutableLiveData()
    var dollarSum:LiveData<Int?> = MutableLiveData()



    lateinit var linpanSum:LiveData<Int?>



    init {
        getUnPaidSum()
        getPaidSum()
        getBarrelSum()
        getTotalMaintance()
        getTotalD()
        getTotalLP()
        Timber.tag("timberlog").e("OnInitViewModel")
    }

    fun getPaidSum() {
        viewModelScope.launch(Dispatchers.IO) {
               paidSum = repository.getPaidSum().asLiveData()
        }
    }

    fun getUnPaidSum() {
        viewModelScope.launch(Dispatchers.IO) {
            unPaidSum = repository.getUnPaidSum().asLiveData()
        }
    }

    fun getBarrelSum() {
        viewModelScope.launch (Dispatchers.IO){
            barrelSum = repository.getBarrelTotals().asLiveData()
        }
    }

    fun getTotalD() {
        viewModelScope.launch {
            dollarSum = repository.getTotalDollar().asLiveData()
        }
    }

    fun getTotalLP() {
        viewModelScope.launch {
            linpanSum = repository.getTotalLinPan().asLiveData()
        }
    }

    fun getTotalMaintance() {
        viewModelScope.launch(Dispatchers.IO) {
            maintenanceSum = repository.getTotalMaintenance().asLiveData()
        }
    }

    fun combineListen():LiveData<ArrayList<Int?>> {
        val mediatorLiveData = MediatorLiveData<ArrayList<Int?>>()
        val paid =  paidSum
        val unPaid = unPaidSum
        val maintenance = maintenanceSum
        val petrol = barrelSum
        var isPaidEmitted = false
        var isUnPaidEmitted =false
        var isMainEmitted = false
        var isPetrolEmitted = false

        mediatorLiveData.addSource(paid) {
            isPaidEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isPetrolEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }

        mediatorLiveData.addSource(unPaid) {
            isUnPaidEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isPetrolEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }
        mediatorLiveData.addSource(maintenance) {
            isMainEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isPetrolEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }
        mediatorLiveData.addSource(petrol) {
            isPetrolEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isPetrolEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }

        return mediatorLiveData
    }



}