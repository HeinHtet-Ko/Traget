package com.mtu.ceit.hhk.traget.ui.maintain

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import com.mtu.ceit.hhk.traget.repos.MaintainRepository
import com.mtu.ceit.hhk.traget.util.DIALOG_EVENT
import com.mtu.ceit.hhk.traget.util.MAIN_EVENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MaintenanceViewModel @Inject constructor(private val repository: MaintainRepository):ViewModel() {

    private val _maintains = MutableLiveData<List<Maintenance>> ()
    val maintains:LiveData<List<Maintenance>> = _maintains

    private val mainEventChannel = Channel<MAIN_EVENT>()
    private val dialogChannel = Channel<DIALOG_EVENT>()
    val mainEventFlow = mainEventChannel.receiveAsFlow()
    val dialogFlow = dialogChannel.receiveAsFlow()

    var editMaintain:MutableLiveData<Maintenance?> = MutableLiveData(null)

    var maintainName:MutableLiveData<String?> = MutableLiveData(null)
    var maintainPrice:MutableLiveData<Int?> = MutableLiveData(null)


    private fun insertMaintain(maintain:Maintenance)
    {
        viewModelScope.launch {
            repository.insertMaintenance(maintain)
        }
    }


     fun getMaintainList() {
        viewModelScope.launch {
            _maintains.value = repository.getMaintains().first()

        }
    }


    fun updateMaintain(maintain: Maintenance) {
        viewModelScope.launch {
            repository.updateMaintain(maintain)
            mainEventChannel.send(MAIN_EVENT.HIDE_DIALOG)
            getMaintainList()
        }

        editMaintain.value = null
    }

    fun onFABClick(){
        viewModelScope.launch {
            mainEventChannel.send(MAIN_EVENT.SHOW_DIALOG)
        }

    }



    fun onMaintainItemClick(maintain: Maintenance){

        editMaintain.value = maintain

        viewModelScope.launch {
            mainEventChannel.send(MAIN_EVENT.SHOW_DIALOG)
            dialogChannel.send(DIALOG_EVENT.BIND_EDITTEXT(maintain))


        }


    }

    fun onSubmitClick(){

        val name = maintainName.value
        val price = maintainPrice.value

        viewModelScope.launch {
            if(name!=null && price != null){
                insertMaintain(Maintenance(name = name,price = price))
                mainEventChannel.send(MAIN_EVENT.HIDE_DIALOG)
                getMaintainList()
            }
            else {
                if(name.isNullOrEmpty())
                    dialogChannel.send(DIALOG_EVENT.SHOW_ERROR("Name Field Required!"))
                else{
                    dialogChannel.send(DIALOG_EVENT.SHOW_ERROR("Price Field Required!"))
                    }
            }
        }


    }

    fun onEditClick(){

        val name = maintainName.value!!
        val price = maintainPrice.value!!
        val new = editMaintain.value?.copy(name = name,price = price)
        new?.let {
            updateMaintain(it)
        }

    }

    fun onCancelClick(){
        viewModelScope.launch {
            mainEventChannel.send(MAIN_EVENT.HIDE_DIALOG)
        }
    }



}