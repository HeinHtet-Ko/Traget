package com.mtu.ceit.hhk.traget.ui.setting

import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import com.mtu.ceit.hhk.traget.repos.SettingRepository
import com.mtu.ceit.hhk.traget.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.File

import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor (
        private val repository: SettingRepository
        ) :ViewModel() {

    private val _bottomNavigationVisibility:MutableLiveData<Int> = MutableLiveData<Int>()
    val bottomNavigationVisibility:LiveData<Int> = _bottomNavigationVisibility


    private val fbStorage = Firebase.storage


    val isNight = repository.isNightMode().asLiveData()
    val hrPrice = repository.getHRPrice().asLiveData()
    val rvPrice = repository.getRVPrice().asLiveData()

    val clients by lazy {
        repository.getAllClients()
    }

    val maintains by lazy {
        repository.getAllMaintains()
    }

    val diesels by lazy {
        repository.getAllDiesels()
    }

    lateinit var clientList:List<Client>
    lateinit var maintainList:List<Maintenance>
    lateinit var dieselList:List<Diesel>

    private val loadingEventChannel:Channel<LoadingEvent> = Channel()
     val loadingEventFlow = loadingEventChannel.receiveAsFlow()

    fun hideBottonNavigation() {
                viewModelScope.launch {
                    _bottomNavigationVisibility.value = View.GONE
                }
            }

    fun showBottomNavigation() {
                viewModelScope.launch {
                    _bottomNavigationVisibility.value = View.VISIBLE
                }
            }

    fun setNightMode(isNight:Boolean) {
           viewModelScope.launch {
               repository.setNightMode(isNight)
           }
        }

    fun setRvPrice(price:Int) {
            viewModelScope.launch {
                repository.setRVPrice(price)
            }
        }

    fun setHrPrice(price:Int) {
            viewModelScope.launch {
                repository.setHRPrice(price)
            }
        }

    fun insertClient(client: Client) {

        viewModelScope.launch {
            repository.insertClient(client)
        }


    }

    fun writeEntireDatabase(clientCSV:File,maintainCSV:File,dieselCSV:File) {

        viewModelScope.launch(IO) {

            loadingEventChannel.send(LoadingEvent.Uploading)

            clientList = clients.first()
            maintainList = maintains.first()
            dieselList = diesels.first()

            CSV_Writer.writeCSV(clientCSV,clientList)
            CSV_Writer.writeCSV(maintainCSV,maintainList)
            CSV_Writer.writeCSV(dieselCSV,dieselList)




            uploadBatch(clientCSV,maintainCSV,dieselCSV)

            }

    }

    private val clientListener:(Client)->Unit = {
          viewModelScope.launch {
              repository.insertClient(it)
          }
    }

    private val maintainListener:(Maintenance) ->(Unit) = {
        viewModelScope.launch {
            repository.insertMaintain(it)
        }
    }

    private val dieselListener:(Diesel) -> Unit = {
        viewModelScope.launch {
            repository.insertDiesel(it)
        }
    }

    fun readEntireDatabase(clientCSV:File,maintainCSV:File,dieselCSV:File){






        viewModelScope.launch(IO) {
            loadingEventChannel.send(LoadingEvent.Downloading)
            downloadFileAndRead(maintainCSV,Constants.maintainFileName)
            downloadFileAndRead(dieselCSV,Constants.dieselFileName)
            downloadFileAndRead(clientCSV,Constants.clientFileName)

        }
    }


    private fun uploadFile(csvFile: File,fileName:String): UploadTask {

        val ref =  fbStorage.reference.child(fileName)

        Timber.tag("fafteo").e(Thread.currentThread().name + csvFile.name + "uploading" + System.currentTimeMillis().toString() )

        return ref.putFile(Uri.fromFile(csvFile))

    }

    private fun uploadBatch(clientCSV: File,maintainCSV: File,dieselCSV: File){

       val taskClient =  uploadFile(clientCSV,Constants.clientFileName)
       val maintainClient =  uploadFile(maintainCSV,Constants.maintainFileName)
       val dieselClient =  uploadFile(dieselCSV,Constants.dieselFileName)



       val tasks= Tasks.whenAll(listOf(taskClient,maintainClient,dieselClient))
               .addOnSuccessListener {
                   viewModelScope.launch {
                       loadingEventChannel.send(LoadingEvent.Uploaded)
                   }


               }
               .addOnCanceledListener {
                   Timber.tag("fafteo").e("cancel")
               }
               .addOnCompleteListener {
                   Timber.tag("fafteo").e("complete")
               }
               .addOnFailureListener {
                   Timber.tag("fafteo").e(it.message)
               }

    }


    private suspend fun downloadFileAndRead(csvFile: File,fileName: String){
        val ref  = fbStorage.reference.child(fileName)

        val task =   ref.getFile(csvFile).await()

        Timber.tag("readeo").e("$fileName downloadfile and read ${Thread.currentThread().name}")

        when(fileName) {
                    Constants.dieselFileName -> {
                        CSV_Writer.readDieselCSV(csvFile,dieselListener)
                    }
                    Constants.maintainFileName -> {
                        CSV_Writer.readMaintainCSV(csvFile,maintainListener)
                    }
                    Constants.clientFileName -> {
                        CSV_Writer.readClientCSV(csvFile,clientListener)
                        loadingEventChannel.send(LoadingEvent.Downloaded)
                    }
                }

    }


        }

sealed class LoadingEvent() {
    object Uploading:LoadingEvent()
    object Downloading:LoadingEvent()
    object Uploaded:LoadingEvent()
    object Downloaded:LoadingEvent()
}

