package com.mtu.ceit.hhk.traget.presentation.features.clients

import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.repos.ClientRepository
import com.mtu.ceit.hhk.traget.data.repos.DISPLAY_STATUS
import com.mtu.ceit.hhk.traget.data.repos.PAY_STATUS
import com.mtu.ceit.hhk.traget.data.repos.SORT_STATUS
import com.mtu.ceit.hhk.traget.domain.usecases.clients.*


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    //private val repository: ClientRepository,
    private val getAllClients: GetAllClients,
    private val createClient: CreateClient,
    private val deleteClient: DeleteClient,
    private val updateClient: UpdateClient,
    private val searchClients: SearchClients,
    private val getDisplayStatus: GetDisplayStatus,
    ):ViewModel() {


    val searchQuery:MutableLiveData<String>  = MutableLiveData("")
    val sort:MutableLiveData<SORT_STATUS> = MutableLiveData(SORT_STATUS.SortByDate)

    var displayStatus:MutableLiveData<DISPLAY_STATUS> = MutableLiveData()

    init {
        getDisplayStatus()
        Timber.tag("vmin").e("vmini")

    }



    @ExperimentalCoroutinesApi
    var clients:LiveData<List<Client>> = combine(searchQuery.asFlow(),sort.asFlow(),displayStatus.asFlow()){
        search,sort,displayStatus -> Triple(search,sort,displayStatus)
    }.flatMapLatest {
        triple ->
//        repository.getSearch(triple.first,triple.second,triple.third)
        searchClients.execute(triple)
    }.asLiveData()

    private fun getDisplayStatus(){
        viewModelScope.launch {
            val status = getDisplayStatus.execute(Unit).first()
            displayStatus.value = status
        }
    }


    fun onChooseHidePaid(){
        viewModelScope.launch {
          //  repository.setDisplayStatus(DISPLAY_STATUS.HIDE_PAID)
        }
    }

    fun onChooseHideUnPaid(){
        viewModelScope.launch {
        //    repository.setDisplayStatus(DISPLAY_STATUS.HIDE_UNPAID)

        }
    }

    fun onChooseShowAll(){
        viewModelScope.launch {
          //  repository.setDisplayStatus(DISPLAY_STATUS.SHOW_ALL)
        }
    }

    fun removeClient(client: Client) {
        viewModelScope.launch {
            deleteClient.execute(client)
        }
    }

//    fun onSwipe(id:Int,isPaid:Boolean){
//    //    viewModelScope.launch {
//            if (isPaid)
//             //   repository.changePayStatus(PAY_STATUS.ToPaid,id)
//            else
//              //  repository.changePayStatus(PAY_STATUS.ToUnPaid,id)
//
//     //   }
//    }

    fun insertClient(client: Client){
        viewModelScope.launch {
            Timber.tag("clienttracker").e(client.toString())
            createClient.execute(client)

        }
    }

}