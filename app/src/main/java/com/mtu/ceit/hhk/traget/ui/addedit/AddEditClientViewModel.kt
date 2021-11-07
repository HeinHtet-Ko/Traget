package com.mtu.ceit.hhk.traget.ui.addedit

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.repos.AddEditClientRepository
import com.mtu.ceit.hhk.traget.util.Constants
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddEditClientViewModel @Inject constructor( savedStateHandle: SavedStateHandle, private val repository: AddEditClientRepository):ViewModel() {


    val client = savedStateHandle.get<Client>("client")

    val isNew:Boolean = client == null

    private val _activeId:MutableLiveData<Int> = MutableLiveData()
    val activeID:LiveData<Int> = _activeId

    private val addEditClientEvent = Channel<AddEditClientEvent>()
     val addEditClientEventFlow = addEditClientEvent.receiveAsFlow()

     val errorMes:MutableLiveData<String> = MutableLiveData()
    private val isAnyError  = !errorMes.value.isNullOrEmpty()

     val newClient:MutableLiveData<Client> = MutableLiveData()

    var hrPrice:Int = 0
    var rvPrice:Int = 0

    init {
        getActive()
        viewModelScope.launch {
            hrPrice = repository.getHrPrice().first()
            rvPrice = repository.getRvPrice().first()
        }

    }

    private fun getActive(){
        viewModelScope.launch {
            _activeId.value = repository.getActive()
        }
    }

    fun onSubmitClick(){
        viewModelScope.launch {
            if(!errorMes.value.isNullOrEmpty())
            {
                addEditClientEvent.send(AddEditClientEvent.ShowInvalidMessage(errorMes.value!!))
            }
            else{

                if(isNew){
                    newClient.value?.let { repository.addClient(it) }
                    addEditClientEvent.send(AddEditClientEvent.NavigateBack(Constants.ADD_CLIENT_OK))
                }else{
                    newClient.value?.let { repository.updateClient(client!!.copy(name = it.name,macType = it.macType,timeTaken = it.timeTaken,amount = it.amount,barrelId = it.barrelId,note = it.note)) }
                    addEditClientEvent.send(AddEditClientEvent.NavigateBack(Constants.EDIT_CLIENT_OK))
                }
            }
        }
    }

    fun onCancelClick(){
        viewModelScope.launch {
            addEditClientEvent.send(AddEditClientEvent.NavigateBack(Constants.CANCEL_CLIENT_OK))
        }

    }

    fun onTimeStartIconClick(){
        viewModelScope.launch {
            addEditClientEvent.send(AddEditClientEvent.ShowTimePicker)
        }
    }


    sealed class AddEditClientEvent {
        data class ShowInvalidMessage(val message:String):AddEditClientEvent()
        data class NavigateBack(val flag:Int):AddEditClientEvent()
        object ShowTimePicker:AddEditClientEvent()
    }

}