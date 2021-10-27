package com.mtu.ceit.hhk.traget.ui.diesel

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.data.model.DieselWithClients
import com.mtu.ceit.hhk.traget.repos.DieselRepository
import com.mtu.ceit.hhk.traget.util.DIALOG_EVENT
import com.mtu.ceit.hhk.traget.util.MAIN_EVENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DieselViewModel @Inject constructor(private val repository: DieselRepository):ViewModel  (){

    var barrels:LiveData<List<Diesel>> = MutableLiveData(emptyList())
    var barrelWithClients:MutableLiveData<List<DieselWithClients>> = MutableLiveData()
    var active_diesel:LiveData<Int> = MutableLiveData()

    var new_price:MutableLiveData<Int?> = MutableLiveData(null)



    private val mainEvent = Channel<MAIN_EVENT>()
    var mainEventFlow = mainEvent.receiveAsFlow()

    private val dialogEvent = Channel<DIALOG_EVENT>()
    val dialogEventFlow =  dialogEvent.receiveAsFlow()


    init {

        getBarrelsWithClients()

    }
     private fun getBarrels()   =
         viewModelScope.launch() {
             barrels = repository.getAllBarrels().asLiveData()
             Timber.tag("barrel").e(" null ${barrels.value?.size}")

     }

     private fun getActiveId() = viewModelScope.launch {
        active_diesel = repository.getActiveId().asLiveData()
         Timber.tag("diselclient").e("${active_diesel.value}  haha")
     }

    private fun buyBarrel(diesel:Diesel){
        viewModelScope.launch {

            repository.insertBarrel(diesel)

            mainEvent.send(MAIN_EVENT.HIDE_DIALOG)

            getBarrelsWithClients()

        }
    }

    fun setActiveId(id:Int) = viewModelScope.launch {
        repository.setAllInactive()
        repository.setActiveId(id)
        getBarrelsWithClients()

    }



     private fun getBarrelsWithClients() {
         viewModelScope.launch(IO) {
            barrelWithClients.postValue(repository.getBarrelsWithClients())
         }
     }

    fun onFabClick(){
        viewModelScope.launch {

            mainEvent.send(MAIN_EVENT.SHOW_DIALOG)
        }

    }

    fun onSubmitClick(){
        if (new_price.value == null)
        {
            viewModelScope.launch {
                Timber.tag("errormessagetrk").e("here it works")
                dialogEvent.send(DIALOG_EVENT.SHOW_ERROR("Put a price first!"))
            }
        }
        Timber.tag("errormessagetrk").e("here work it")

        new_price.value?.let { price ->
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