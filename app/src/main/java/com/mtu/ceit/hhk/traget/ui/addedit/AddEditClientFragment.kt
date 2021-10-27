package com.mtu.ceit.hhk.traget.ui.addedit


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.util.Utils
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.databinding.FragmentAddEditClientBinding
import com.mtu.ceit.hhk.traget.util.setext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.text.DateFormat

@AndroidEntryPoint
class AddEditClientFragment:Fragment(R.layout.fragment_add_edit_client) {


    private val vm by viewModels<AddEditClientViewModel>()
    lateinit var  binding:FragmentAddEditClientBinding
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEditClientBinding.bind(view)


        viewComponentsInit()



        if(vm.client == null )
        observeActiveDiesel()

        clickListen()

        collect()

        observePrice()

        binding.apply {
            vm.client?.let {


                frAddEditNameEdT.setText(it.name)
                frAddEditNoteEdtext.setText(it.note)
                frAddEditDieselNoEdT.setText(it.barrelId.toString())
                frAddEditDateTv.text =  "Created at - ${DateFormat.getDateInstance().format(it.date)}"


                val checkID = when(it.macType) {
                    Utils.ROTAVATOR -> R.id.fr_addEdit_rv_chip
                    Utils.HARROW -> R.id.fr_addEdit_hr_chip
                    else -> R.id.fr_addEdit_hr_chip
                }
                frAddEditChipGroup.check(checkID)

               val time =  Utils.formateDate(it.timeTaken)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    frAddEditTp.hour = time.first.toInt()
                    frAddEditTp.minute = time.second.toInt()
                }

            }

        }



    }


    private fun observePrice() {
        vm.rvPrice.observe(viewLifecycleOwner){
            Timber.tag("nummob").e("$it  good ")
        }
        vm.hrPrice.observe(viewLifecycleOwner){

        }

    }



    private fun viewComponentsInit(){
        binding.apply {
            frAddEditTp.setIs24HourView(true)

            frAddEditNoteCb.setOnCheckedChangeListener { _, b ->
                frAddEditNoteEdtext.isEnabled = !b
            }
            frAddEditDieselCb.setOnCheckedChangeListener { _, b ->
                frAddEditDieselNoEdT.isEnabled = !b
            }
        }

    }

    private fun observeActiveDiesel()
    {

        vm.activeID.observe(viewLifecycleOwner){

            binding.frAddEditDieselNoEdT.setText(it?.toString())

        }
    }

    private fun validateInput(){
        binding.apply {

            if(frAddEditNameEdT.text.isNullOrEmpty())
                vm.errorMes.value = "Name Field Required!"
            else {
                var name = ""
                name = frAddEditNameEdT.text.toString().trim()

                var mins = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mins = frAddEditTp.hour * 60
                    mins += frAddEditTp.minute
                } else {
                    mins = frAddEditTp.currentHour * 60
                    mins += frAddEditTp.currentMinute
                }

                var macType = ""
                var amount = 0
                val hrPerMin = vm.hrPrice.value?.div(60)
                val rvPerMin = vm.rvPrice.value?.div(60)

                when (frAddEditChipGroup.checkedChipId) {
                    R.id.fr_addEdit_rv_chip -> {
                        macType = Utils.ROTAVATOR
                        Timber.tag("nummob").e(rvPerMin.toString()+"rv")
                        amount = mins * rvPerMin!!
                    }
                    R.id.fr_addEdit_hr_chip -> {
                        macType = Utils.HARROW
                        Timber.tag("nummob").e(hrPerMin.toString()+"hr")
                        amount = mins * hrPerMin!!
                    }
                }

                val dId = frAddEditDieselNoEdT.text.toString().toInt()

                val note = frAddEditNoteEdtext.text.toString().trim()

                vm.newClient.value = Client(name = name, timeTaken = mins, amount = amount, macType = macType, barrelId = dId,note = note)

            }
        }
    }

    private fun clickListen(){

        binding.apply {

            frAddEditConfirmBtn.setOnClickListener {

                    validateInput()
                    vm.onSubmitClick()
            }
            frAddEditCancelBtn.setOnClickListener {
                    findNavController().popBackStack()
            }
        }
    }

    private fun collect(){
        lifecycleScope.launchWhenStarted {

            vm.addEditClientEventFlow.collect {
                when(it) {
                    is AddEditClientViewModel.AddEditClientEvent.ShowInvalidMessage ->{
                        Snackbar.make(requireView(),it.message,3000).show()
                    }
                    is AddEditClientViewModel.AddEditClientEvent.NavigateBack -> {

                        findNavController().popBackStack()
                    }
                }
            }
        }
    }










}