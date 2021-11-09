package com.mtu.ceit.hhk.traget.ui.addedit


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.databinding.FragmentAddEditClientBinding
import com.mtu.ceit.hhk.traget.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DateFormat

@AndroidEntryPoint
class AddEditClientFragment:Fragment(R.layout.fragment_add_edit_client) {


    private val vm by viewModels<AddEditClientViewModel>()
    lateinit var  binding:FragmentAddEditClientBinding

    val addTimebs by lazy { AddTimeBottomSheet() }

    private var timeMins:Int = 0
    private var macItemPos:Int = -1
    private var amount:Int = 0

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEditClientBinding.bind(view)


        viewComponentsInit()



        if(vm.isNew){
            observeActiveDiesel()
        }else{
            bindUi(vm.client!!)
        }

        collectEvents()

    }
    private fun viewComponentsInit()
    {
        binding.apply {

            frAddEditAddTimeLayout.setStartIconOnClickListener {
                vm.onTimeStartIconClick()
            }

            val macList = listOf(getString(R.string.hr_str), getString(R.string.rv_str))
            val adapter = context?.let { ArrayAdapter(it, R.layout.mac_type_item, macList) }
            frAddEditMacAutocomplete.setAdapter(adapter)
            frAddEditMacAutocomplete.setText(macList[0],false)

            frAddEditDieselLayout.setStartIconOnClickListener {
                frAddEditDieselNoEdt.apply {
                    isEnabled = !isEnabled
                }
            }
            frAddEditNoteLayout.setStartIconOnClickListener {
                frAddEditNoteEdtext.apply {
                    isEnabled = !isEnabled
                }
            }


            frAddEditConfirmBtn.setOnClickListener {
                gatherInputs()
                vm.onSubmitClick()
            }
            frAddEditCancelBtn.setOnClickListener {
                vm.onCancelClick()
            }

            AddTimeBottomSheet.timeChangeListener = { hr, min ->

                timeMins = (hr * 60) + min
                val str = getString(R.string.timetaken_str,hr,min)
                binding.frAddEditTimeEdT.setText(str)

            }

            frAddEditMacAutocomplete.setOnItemClickListener { _, _, position, _ ->

                macItemPos = position


            }

            frAddEditDateTv.text = getString(R.string.created_date_str,DateFormat.getDateInstance().format(System.currentTimeMillis()).toString())

        }

    }

    private fun bindUi(client: Client)
    {
        binding.apply {
            frAddEditNameEdt.setText(client.name)
            frAddEditNoteEdtext.setText(client.note)
            val pair = Utils.formateDate(client.timeTaken)
            val str = getString(R.string.timetaken_str,pair.first.toInt(),pair.second.toInt())
            frAddEditTimeEdT.setText(str)
            frAddEditDieselNoEdt.setText(client.barrelId.toString())
            frAddEditDateTv.text = getString(R.string.created_date_str,DateFormat.getDateInstance().format(client.date).toString())

            timeMins = client.timeTaken
            macItemPos = if(client.macType == Constants.ROTAVATOR) 1 else 0

            val checkID = when(client.macType) {
                Constants.ROTAVATOR -> R.string.rv_str
                Constants.HARROW -> R.string.hr_str
                else -> R.string.hr_str
            }

            frAddEditMacAutocomplete.setText(getString(checkID),false)
            frAddEditConfirmBtn.text = getString(R.string.editBtn_str)


        }
    }

    private fun observeActiveDiesel()
    {

        vm.activeID.observe(viewLifecycleOwner){

            binding.frAddEditDieselNoEdt.setText(it?.toString())

        }
    }

    private fun gatherInputs()
    {
        binding.apply {
            if(frAddEditNameEdt.text.isNullOrEmpty())
            {
                vm.errorMes.value = "Name Field Required!"

            }
            else if (timeMins == 0 && frAddEditTimeEdT.text.isNullOrEmpty() ){
                vm.errorMes.value = "Time Field Required!"

            }

            else if (frAddEditDieselNoEdt.text.isNullOrEmpty())
            {
                vm.errorMes.value = "Diesel No Required!"
            }
            else {

                vm.errorMes.value = null
                val name = frAddEditNameEdt.text.toString()
                val macType = if (macItemPos == 1) Constants.ROTAVATOR else Constants.HARROW
                val bId = frAddEditDieselNoEdt.text.toString().toInt()
                val note = frAddEditNoteEdtext.text.toString()


                if(macItemPos == 1 ){
                    amount = calculateAmt(vm.rvPrice,timeMins)
                }
                else {
                    amount = calculateAmt(vm.hrPrice,timeMins)
                }

                vm.newClient.value = Client(name = name,timeTaken = timeMins,macType = macType,amount = amount,barrelId = bId,note = note)

            }
        }
    }

    private fun calculateAmt(price:Int,timeMin:Int): Int
    {

        return price.div(60).times(timeMin)
    }

    private fun collectEvents()
    {
        lifecycleScope.launch {

            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    vm.addEditClientEventFlow.collect {
                        when(it) {
                            is AddEditClientViewModel.AddEditClientEvent.ShowInvalidMessage ->{
                                Snackbar.make(requireView(),it.message,3000).show()
                            }
                             is AddEditClientViewModel.AddEditClientEvent.NavigateBack -> {
                                findNavController().popBackStack()
                            }
                            AddEditClientViewModel.AddEditClientEvent.ShowTimePicker -> {
                                addTimebs.show(parentFragmentManager,AddTimeBottomSheet.TAG)
                            }
                        }
                    }
                }

            }


        }
    }

}

