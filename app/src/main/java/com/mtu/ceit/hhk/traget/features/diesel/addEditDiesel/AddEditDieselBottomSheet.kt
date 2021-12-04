package com.mtu.ceit.hhk.traget.features.diesel.addEditDiesel

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.FragmentAddEditDieselBinding
import com.mtu.ceit.hhk.traget.features.diesel.DieselViewModel
import com.mtu.ceit.hhk.traget.util.DIALOG_EVENT
import kotlinx.android.synthetic.main.fragment_add_edit_diesel.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddEditDieselBottomSheet:BottomSheetDialogFragment() {

    private val vm by viewModels<DieselViewModel>(
        ownerProducer = {requireParentFragment()}
    )
    lateinit var binding:FragmentAddEditDieselBinding

    companion object {
        const val TAG = "AddEdit_Diesel_BottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_add_edit_diesel,container,false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEditDieselBinding.bind(view)

        binding.frAddEditDsPriceEdT.addTextChangedListener {

            if(it.toString().isNotEmpty())
            vm.newPrice.value = it.toString().toInt()

        }

        binding.frAddEditDsApplyBtn.setOnClickListener {
            vm.onSubmitClick()
        }

        binding.frAddEditDsCancelBtn.setOnClickListener {
            vm.onCancelClick()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.dialogEventFlow.collect {
                when(it) {
                    is DIALOG_EVENT.SHOW_ERROR -> {
                        Snackbar.make(view.rootView,it.message,Snackbar.LENGTH_LONG).show()

                    }
                }

                }
            }
        }


    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        binding.frAddEditDsPriceEdT.text?.clear()

    }


}