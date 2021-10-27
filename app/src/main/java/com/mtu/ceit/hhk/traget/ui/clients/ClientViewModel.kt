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


    var clients:MutableLiveData<List<Client>> = MutableLiveData()

    val searchQuery:MutableLiveData<String>  = MutableLiveData("")
    val sort:MutableLiveData<SORT> = MutableLiveData()

   // val isHidePaid:MutableLiveData<Boolean> = MutableLiveData()

    var displayStatus:MutableLiveData<DISPLAY_STATUS> = MutableLiveData()




    init {
        getDisplayStatus()

       // observe()

    }



    var _clients:LiveData<List<Client>> = combine(searchQuery.asFlow(),sort.asFlow(),displayStatus.asFlow()){
        search,sort,displayStatus -> Triple(search,sort,displayStatus)
    }.flatMapLatest {
        triple ->
        repository.getSearch(triple.first,triple.second,triple.third)
    }.asLiveData()

    private fun getDisplayStatus(){
        viewModelScope.launch {

            val isHidePaid = repository.isHidePaid().first()
            val isHideUnPaid = repository.isHideUnPaid().first()

            if(isHidePaid) displayStatus.value = DISPLAY_STATUS.HIDE_PAID
            if(isHideUnPaid) displayStatus.value = DISPLAY_STATUS.HIDE_UNPAID
            if((!isHidePaid && !isHideUnPaid)) {

                displayStatus.value = DISPLAY_STATUS.SHOW_ALL
                Timber.tag("clientinit").e("initial $isHidePaid $isHideUnPaid")
            }


        }
    }

    private fun observe(){
        viewModelScope.launch {

            displayStatus.asFlow().collect {
                    when(it) {
                        DISPLAY_STATUS.HIDE_PAID ->
                        {
                            repository.hidePaid(true)
                            repository.hideUnPaid(false)
                        }

                        DISPLAY_STATUS.HIDE_UNPAID -> {
                            repository.hideUnPaid(true)
                            repository.hidePaid(false)
                        }
                    }
            }
        }

    }

    fun hideUnPaid(isHide:Boolean) {
        viewModelScope.launch {
            repository.hideUnPaid(isHide)
        }
    }

     fun hidePaid(isHide:Boolean) {
         viewModelScope.launch {
             repository.hidePaid(isHide)
         }

    }

    fun removeClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
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