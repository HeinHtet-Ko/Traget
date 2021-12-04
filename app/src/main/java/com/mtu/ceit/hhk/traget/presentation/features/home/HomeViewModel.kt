package com.mtu.ceit.hhk.traget.presentation.features.home


import androidx.lifecycle.*
import com.mtu.ceit.hhk.traget.data.repos.HomeRepository
import com.mtu.ceit.hhk.traget.domain.usecases.home.GetPaidSum
import com.mtu.ceit.hhk.traget.domain.usecases.home.GetTotalHr
import com.mtu.ceit.hhk.traget.domain.usecases.home.GetTotalRo
import com.mtu.ceit.hhk.traget.domain.usecases.home.GetUnpaidSum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val getPaidSum: GetPaidSum,
    private val getUnpaidSum: GetUnpaidSum,
    private val getTotalHr: GetTotalHr,
    private val getTotalRo: GetTotalRo
) :ViewModel(){



    private val _paidSum:MutableLiveData<Int> = MutableLiveData()
    val paidSum:LiveData<Int> = _paidSum

    private val _unPaidSum:MutableLiveData<Int> = MutableLiveData()
    val unPaidSum:LiveData<Int> = _unPaidSum

    private val _dieselSum:MutableLiveData<Int> = MutableLiveData()
    val dieselSum:LiveData<Int> = _dieselSum

    private val _mainTainSum:MutableLiveData<Int> = MutableLiveData()
    val mainTainSum:LiveData<Int> = _mainTainSum

    private val _rvSum:MutableLiveData<Int> = MutableLiveData()
    val rvSum:LiveData<Int> = _rvSum

    private val _hrSum:MutableLiveData<Int> = MutableLiveData()
    val hrSum:LiveData<Int> = _hrSum



    init {
        getPaidSum()
        getUnPaidSum()
        getDieselSum()
        getTotalMaintance()
        getTotalRotavator()
        getTotalHarrow()

    }

    private fun getPaidSum() {
        viewModelScope.launch{
           val paidSum =  getPaidSum.execute(Unit)
            _paidSum.value = paidSum
        }
    }

    private fun getUnPaidSum() {
        viewModelScope.launch{
            val unpaidSum = getUnpaidSum.execute(Unit)
            _unPaidSum.postValue(unpaidSum)
        }
    }

    private fun getDieselSum() {
        viewModelScope.launch(IO) {
            _dieselSum.postValue(repository.getDieselTotal())
        }
    }

    private fun getTotalMaintance() {
        viewModelScope.launch(IO) {
            _mainTainSum.postValue(repository.getTotalMaintenance())
        }
    }

    private fun getTotalRotavator() {
        viewModelScope.launch {
            val totalRo = getTotalRo.execute(Unit)
            _rvSum.value = totalRo
           // rvSum = repository.getTotalRo().asLiveData()
        }
    }

    private fun getTotalHarrow() {
        viewModelScope.launch {
            val totalHr = getTotalHr.execute(Unit)
            _hrSum.value = totalHr
        }
    }

    fun combineTime():LiveData<ArrayList<Int?>> {
        val mediatorLiveData = MediatorLiveData<ArrayList<Int?>>()
        val hr = hrSum
        val rv = rvSum
        var isHrEmitted = false
        var isRvEmitted = false

        mediatorLiveData.addSource(rv){
            isRvEmitted = true
            if(isHrEmitted && isRvEmitted)
                mediatorLiveData.value = arrayListOf(rv.value,hr.value)
        }
        mediatorLiveData.addSource(hr){
            isHrEmitted = true
            if(isHrEmitted && isRvEmitted)
                mediatorLiveData.value = arrayListOf(rv.value,hr.value)
        }

        return mediatorLiveData
    }

    fun combineAmt():LiveData<ArrayList<Int?>> {
        val mediatorLiveData = MediatorLiveData<ArrayList<Int?>>()
        val paid = _paidSum
        val unPaid = _unPaidSum
        val maintenance = _mainTainSum
        val petrol = _dieselSum
        var isPaidEmitted = false
        var isUnPaidEmitted =false
        var isMainEmitted = false
        var isDieselEmitted = false

        mediatorLiveData.addSource(paid) {
            isPaidEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isDieselEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }

        mediatorLiveData.addSource(unPaid) {
            isUnPaidEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isDieselEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }
        mediatorLiveData.addSource(maintenance) {
            isMainEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isDieselEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }
        mediatorLiveData.addSource(petrol) {
            isDieselEmitted = true
            if (isPaidEmitted && isUnPaidEmitted && isMainEmitted && isDieselEmitted) {
                mediatorLiveData.value = arrayListOf(paid.value, unPaid.value, maintenance.value,petrol.value)
            }
        }

        return mediatorLiveData
    }



}