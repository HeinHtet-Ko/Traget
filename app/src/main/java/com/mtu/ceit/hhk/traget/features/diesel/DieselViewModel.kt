package com.mtu.ceit.hhk.traget.features.diesel

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.repos.DieselRepository
import com.mtu.ceit.hhk.traget.util.DIALOG_EVENT
import com.mtu.ceit.hhk.traget.util.MAIN_EVENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DieselViewModel @Inject constructor(private val repository: DieselRepository):ViewModel  (){


    val bc = repository.getBarrelsWithClients().asLiveData()

    var newPrice:MutableLiveData<Int?> = MutableLiveData(null)

    private val mainEvent = Channel<MAIN_EVENT>()
    var mainEventFlow = mainEvent.receiveAsFlow()

    private val dialogEvent = Channel<DIALOG_EVENT>()
    val dialogEventFlow =  dialogEvent.receiveAsFlow()

    private fun buyBarrel(diesel:Diesel){
        viewModelScope.launch {

            repository.insertBarrel(diesel)
            mainEvent.send(MAIN_EVENT.HIDE_DIALOG)
           // getBarrelsWithClients()

        }
    }



    fun onLongClick(diesel:Diesel){
        viewModelScope.launch {
            repository.deleteDiesel(diesel)
        }
    }



    fun onFabClick(){
        viewModelScope.launch {

            mainEvent.send(MAIN_EVENT.SHOW_DIALOG)
        }

    }

    fun onSubmitClick(){
        if (newPrice.value == null)
        {
            viewModelScope.launch {
                dialogEvent.send(DIALOG_EVENT.SHOW_ERROR("Put a price first!"))
            }
        }

        newPrice.value?.let { price ->
            buyBarrel(Diesel(price = price))

        }
    }

    fun onCancelClick() {
        viewModelScope.launch {
            mainEvent.send(MAIN_EVENT.HIDE_DIALOG)
        }
    }

    fun onEditClick(){

    }



}