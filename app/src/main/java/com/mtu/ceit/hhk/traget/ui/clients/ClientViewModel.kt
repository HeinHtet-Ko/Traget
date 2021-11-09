package com.mtu.ceit.hhk.traget.ui.clients

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.repos.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val repository: ClientRepository):ViewModel() {


    val searchQuery:MutableLiveData<String>  = MutableLiveData("")
    val sort:MutableLiveData<SORT_STATUS> = MutableLiveData(SORT_STATUS.SortByDate)


    var displayStatus:MutableLiveData<DISPLAY_STATUS> = MutableLiveData()

    init {
        getDisplayStatus()

    }



    @ExperimentalCoroutinesApi
    var clients:LiveData<List<Client>> = combine(searchQuery.asFlow(),sort.asFlow(),displayStatus.asFlow()){
        search,sort,displayStatus -> Triple(search,sort,displayStatus)
    }.flatMapLatest {
        triple ->
        repository.getSearch(triple.first,triple.second,triple.third)
    }.asLiveData()

    private fun getDisplayStatus(){
        viewModelScope.launch {
            val status = DISPLAY_STATUS.valueOf(repository.displayStatus().first())
            displayStatus.value = status
        }
    }


    fun onChooseHidePaid(){
        viewModelScope.launch {
            repository.setDisplayStatus(DISPLAY_STATUS.HIDE_PAID)
        }
    }

    fun onChooseHideUnPaid(){
        viewModelScope.launch {
            repository.setDisplayStatus(DISPLAY_STATUS.HIDE_UNPAID)

        }
    }

    fun onChooseShowAll(){
        viewModelScope.launch {
            repository.setDisplayStatus(DISPLAY_STATUS.SHOW_ALL)
        }
    }

    fun removeClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    fun onSwipe(id:Int,isPaid:Boolean){
        viewModelScope.launch {
            if (isPaid)
                repository.changePayStatus(PAY_STATUS.ToPaid,id)
            else
                repository.changePayStatus(PAY_STATUS.ToUnPaid,id)

        }
    }

}