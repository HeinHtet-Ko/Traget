package com.mtu.ceit.hhk.traget.ui

import android.util.Log
import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.Client
import com.mtu.ceit.hhk.traget.data.PaidSum
import com.mtu.ceit.hhk.traget.repos.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val repository: ClientRepository):ViewModel() {


//    private val _clients  = MutableLiveData<List<Client>>()
    lateinit var clients:LiveData<List<Client>>

      var _paidSum:MutableLiveData<PaidSum>  = MutableLiveData()
    lateinit var paidSum:LiveData<PaidSum>


    fun getUnpaidSum() {
        viewModelScope.launch {
            _paidSum.postValue(repository.getPaidSum())  }
    }
    fun getAllClients() {
        viewModelScope.launch {
            clients = repository.getAllClients().asLiveData()
        }
    }

    fun onSumbitClick(name:String,time:Int,amt:Int,type:String){

            viewModelScope.launch {
                repository.insertClient(Client(name = name,timeTaken = time,amount = amt,macType = type))
            }

    }



}