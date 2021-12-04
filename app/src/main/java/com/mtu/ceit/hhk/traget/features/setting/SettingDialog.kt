package com.mtu.ceit.hhk.traget.features.setting

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.DialogSettingValueUpdateBinding


class SettingDialog:DialogFragment(R.layout.dialog_setting_value_update) {

    companion object {
        const val TAG = "SettingDialog"
    }

    lateinit var binding:DialogSettingValueUpdateBinding

     var valueListener:((Int)->Unit)? = null

     var strMap:Pair<String,String>?= null

    private fun setWidth(percentage:Int){
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSettingValueUpdateBinding.inflate(inflater)

        binding.apply {
            frSettingBsCaption.text = strMap?.first
            frSettingBsFeeLy.hint = strMap?.second
        }



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidth(90)

       // dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        binding.apply {

            frSettingBsCancelBtn.setOnClickListener {
                dismiss()
            }
            frSettingBsApplyBtn.setOnClickListener {
                val value = frSettingBsFeeEdt.text.toString()
                if(value.isNotEmpty()){
                    valueListener?.invoke(value.toInt())
                    dismiss()
                }else{
                    frSettingBsFeeLy.error = "Value Needed!"
                   // Snackbar.make(requireView(),"Value Needed!",Snackbar.LENGTH_LONG).show()
                }

            }


        }


    }


}