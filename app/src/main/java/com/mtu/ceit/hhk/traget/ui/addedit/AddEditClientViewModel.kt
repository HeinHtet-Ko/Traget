package com.mtu.ceit.hhk.traget.ui.addedit

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.repos.AddEditClientRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddEditClientViewModel @Inject constructor( val savedStateHandle: SavedStateHandle, private val repository: AddEditClientRepository):ViewModel() {


    val client = savedStateHandle.get<Client>("client")

    private val _activeId:MutableLiveData<Int> = MutableLiveData()
    val activeID:LiveData<Int> = _activeId
     private val addEditClientEvent = Channel<AddEditClientEvent>()
     val addEditClientEventFlow = addEditClientEvent.receiveAsFlow()
    val errorMes:MutableLiveData<String> = MutableLiveData()
    val newClient:MutableLiveData<Client> = MutableLiveData()

    val hrPrice = repository.getHrPrice().asLiveData()
    val rvPrice = repository.getRvPrice().asLiveData()

    init {
        getActive()
      //  Timber.tag("nummob").e(repository.getHrPrice().asLiveData().value.toString()+"inirv")
    }

    private fun getActive(){
        viewModelScope.launch {
            _activeId.value = repository.getActive()
        }
    }

    fun onSubmitClick(){
        viewModelScope.launch {
            if(!errorMes.value.isNullOrEmpty()){
                addEditClientEvent.send(AddEditClientEvent.ShowInvalidMessage(errorMes.value!!))
            }else{

                if(client == null){
                    newClient.value?.let { repository.addClient(it) }
                    addEditClientEvent.send(AddEditClientEvent.NavigateBack(Utils.ADD_CLIENT_OK))
                }else{
                    newClient.value?.let { repository.updateClient(client.copy(name = it.name,macType = it.macType,timeTaken = it.timeTaken,amount = it.amount,barrelId = it.barrelId,note = it.note)) }
                    Timber.tag("newClient").e(newClient.value?.name)
                    addEditClientEvent.send(AddEditClientEvent.NavigateBack(Utils.EDIT_CLIENT_OK))
                }


            }
        }



    }

    sealed class AddEditClientEvent {
        data class ShowInvalidMessage(val message:String):AddEditClientEvent()
        data class NavigateBack(val flag:Int):AddEditClientEvent()
    }

}