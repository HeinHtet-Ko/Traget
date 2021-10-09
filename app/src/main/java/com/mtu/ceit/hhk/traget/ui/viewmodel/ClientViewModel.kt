package com.mtu.ceit.hhk.traget.ui.viewmodel

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.model.Client

import com.mtu.ceit.hhk.traget.repos.ClientRepository
import com.mtu.ceit.hhk.traget.repos.UPDATE_PAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val repository: ClientRepository):ViewModel() {


//    private val _clients  = MutableLiveData<List<Client>>()
    lateinit var clients:LiveData<List<Client>>




    fun removeClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    fun getAllClients() {
        viewModelScope.launch {
            clients = repository.getAllClients().asLiveData()
        }
    }

    fun onSumbitClick(name:String,time:Int,amt:Int,type:String,bID:Int){

            viewModelScope.launch {
               repository.insertClient(Client(name = name,timeTaken = time,amount = amt,macType = type,barrelId = bID))
            }

    }

    fun onSwipeRight(id:Int){
        viewModelScope.launch {
            repository.changePayStatus(UPDATE_PAY.ToPaid,id)
        }
    }

    fun onSwipeLeft(id:Int){
        viewModelScope.launch {
            repository.changePayStatus(UPDATE_PAY.ToUnPaid,id)
        }
    }



}