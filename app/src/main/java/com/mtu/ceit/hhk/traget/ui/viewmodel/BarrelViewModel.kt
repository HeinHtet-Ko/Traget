package com.mtu.ceit.hhk.traget.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.model.Barrel
import com.mtu.ceit.hhk.traget.data.model.BarrelWithClients
import com.mtu.ceit.hhk.traget.repos.BarrelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BarrelViewModel @Inject constructor(private val repository: BarrelRepository):ViewModel  (){

    var barrels:LiveData<List<Barrel>> = MutableLiveData(emptyList())
   val barrelWithClients:MutableLiveData<List<BarrelWithClients>> = MutableLiveData()

    init {
        getBarrels()
    }
     fun getBarrels()   =
         viewModelScope.launch() {
             barrels = repository.getAllBarrels().asLiveData()
             Timber.tag("barrel").e(" null ${barrels.value?.size}")

     }


     fun getBarrelsWithClients() {
         viewModelScope.launch(Dispatchers.IO) {
            barrelWithClients.postValue(repository.getBarrelsWithClients())
         }
     }

     fun buyBarrel(barrel:Barrel){
        viewModelScope.launch {
            repository.insertBarrel(barrel)
        }
    }



}