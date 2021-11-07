package com.mtu.ceit.hhk.traget.ui.maintain.addEditMaintain

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import com.mtu.ceit.hhk.traget.databinding.FragmentAddEditMaintainBinding
import com.mtu.ceit.hhk.traget.ui.maintain.MaintenanceFragment
import com.mtu.ceit.hhk.traget.ui.maintain.MaintenanceViewModel
import com.mtu.ceit.hhk.traget.util.DIALOG_EVENT
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class AddEditMaintainBottomSheet: BottomSheetDialogFragment() {

    lateinit var binding:FragmentAddEditMaintainBinding
    private val vm by activityViewModels<MaintenanceViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_add_edit_maintain,container,false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         binding = FragmentAddEditMaintainBinding.bind(view)

        this.isCancelable = false

         binding.apply {
            frAddEditMtNameEdT.addTextChangedListener {
                vm.maintainName.value = it.toString()
            }
            frAddEditMtPriceEdT.addTextChangedListener {
                if(it.toString().isNotEmpty())
                vm.maintainPrice.value = it.toString().toInt()
            }
        }

        binding.frAddEditMtCancelBtn.setOnClickListener {
            vm.onCancelClick()
        }

        binding.frAddEditMtApplyBtn.setOnClickListener {
            if(vm.editMaintain.value == null)
                vm.onSubmitClick()
            else
                vm.onEditClick()

        }
        collectEvents()
    }

    private fun collectEvents(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

                vm.dialogFlow.collect {
                    when(it) {
                        is DIALOG_EVENT.BIND_EDITTEXT<*>->
                            setEditText(it.data as Maintenance)
                        is DIALOG_EVENT.SHOW_ERROR ->
                            Snackbar.make(requireView().rootView,it.message,Snackbar.LENGTH_LONG).show()

                    }
                }
            }

        }
    }

    private fun setEditText(maintain:Maintenance){

        binding.apply {
            frAddEditMtPriceEdT.setText(maintain.price.toString())
            frAddEditMtNameEdT.setText(maintain.name)
            frAddEditMtApplyBtn.text = getString(R.string.editBtn_str)
            frAddEditMtLabel.text = "Edit A Maintenance "
        }

    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.frAddEditMtNameEdT.text!!.clear()
        binding.frAddEditMtPriceEdT.text!!.clear()

    }




    companion object {
        const val TAG = "AddEdit_Maintain_BottomSheet"
    }

}