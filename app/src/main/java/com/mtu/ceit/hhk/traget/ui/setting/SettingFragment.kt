package com.mtu.ceit.hhk.traget.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mtu.ceit.hhk.traget.R

import com.mtu.ceit.hhk.traget.databinding.FragmentSettingsBinding
import com.mtu.ceit.hhk.traget.ui.MainActivity
import com.mtu.ceit.hhk.traget.util.Constants
import com.mtu.ceit.hhk.traget.util.DialogBuilder
import com.mtu.ceit.hhk.traget.util.Utils
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment:Fragment(R.layout.fragment_settings) {


    lateinit var binding: FragmentSettingsBinding
    private lateinit var vm :SettingViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        vm = (requireActivity() as MainActivity).settingVM


        observeTheme()
        observeLang()
        observePrice()
        viewInit()




    }


    private fun viewInit(){
        binding.frSettingThemeLy.setOnClickListener {

            val listenerTheme = { i:Int ->
                vm.setNightMode(i==0)
                setThemeImage(i==0)
            }

            val array = arrayOf("  Dark Theme  ","  Day Theme  ")
            DialogBuilder.buildItemDialog(requireContext(),array,listenerTheme,"Choose App Theme",R.drawable.darkday)





        }
        binding.frSettingLangLy.setOnClickListener {
            val listener = { i:Int ->
                val lang = if (i==0) Constants.ENGLISH else Constants.BURMESE
                setLangImage(i==0)
                setLocale(lang)




            }

            val array = arrayOf("English","Burmese")
            DialogBuilder.buildItemDialog(requireContext(),array,listener,"Choose App Language",R.drawable.language)

        }

        binding.frSettingRvfeeLy.setOnClickListener {

            showRvBs()

        }

        binding.frSettingHrfeeLy.setOnClickListener {
            showHrBs()
        }


    }

    private fun showBs(listener:(Int)->(Unit)){
        val bs = UpdateFeeBottomSheet()
        if(!bs.isAdded)
            bs.show(parentFragmentManager,UpdateFeeBottomSheet.TAG)

        bs.listener = listener

    }

    private fun showRvBs(){

        val listener = { updatedFee:Int ->



           // binding.frSettingRvfeeTv.text = getString(R.string.client_amt_str,updatedFee)
            vm.setRvPrice(updatedFee)
        }

        showBs(listener)

    }

    private fun showHrBs(){
        val listener = {updatedFee:Int ->
           // binding.frSettingHrfeeTv.text = getString(R.string.client_amt_str,updatedFee)
            vm.setHrPrice(updatedFee)
        }
        showBs(listener)
    }

    private fun observeTheme(){
        vm.isNight.observe(viewLifecycleOwner){
            setThemeImage(it)
        }
    }

    private fun observeLang(){
        val lang = Lingver.getInstance().getLanguage()

        setLangImage(lang == Constants.ENGLISH)

    }

    private fun observePrice(){

        vm.hrPrice.observe(viewLifecycleOwner){
            binding.frSettingHrfeeTv.text = getString(R.string.client_amt_str,it)
        }
        vm.rvPrice.observe(viewLifecycleOwner){
            binding.frSettingRvfeeTv.text = getString(R.string.client_amt_str,it)
        }

    }

    private fun setThemeImage(isNight:Boolean){
        if (isNight)
            binding.frSettingThemeImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.night_mode))
        else
            binding.frSettingThemeImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.daymode))
    }

    private fun setLangImage(isEnglish:Boolean){
        if (isEnglish)
            binding.frSettingLangImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.english))
        else
            binding.frSettingLangImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.burmese))


    }

    private fun setLocale(lang:String){
        val lg = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            requireContext().resources.configuration.locales[0].language
        } else {
            requireContext().resources.configuration.locale.language
        }
        if(lg!=lang){
            Lingver.getInstance().setLocale(requireContext(),lang)
            requireActivity().recreate()
        }

    }





}