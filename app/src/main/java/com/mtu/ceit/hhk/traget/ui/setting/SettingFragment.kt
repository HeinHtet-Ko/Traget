package com.mtu.ceit.hhk.traget.ui.setting

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import com.mtu.ceit.hhk.traget.R

import com.mtu.ceit.hhk.traget.databinding.FragmentSettingsBinding
import com.mtu.ceit.hhk.traget.ui.MainActivity
import com.mtu.ceit.hhk.traget.util.Constants
import com.mtu.ceit.hhk.traget.util.DialogBuilder
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class SettingFragment:Fragment(R.layout.fragment_settings) {


    lateinit var binding: FragmentSettingsBinding
    private lateinit var vm :SettingViewModel


    private lateinit var localClientFile:File
    private lateinit var localMaintainFile:File
    private lateinit var localDieselFile:File

    lateinit var ad:AlertDialog




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        vm = (requireActivity() as MainActivity).settingVM

        observeTheme()
        observeLang()
        observePrice()
        viewInit()

        collectLoadingEvent()

        localClientFile = generateFile(requireContext(),Constants.clientFileName)
        localMaintainFile = generateFile(requireContext(),Constants.maintainFileName)
        localDieselFile = generateFile(requireContext(),Constants.dieselFileName)





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

        binding.frSettingUploadLy.setOnClickListener {
            vm.writeEntireDatabase(localClientFile,localMaintainFile,localDieselFile)
            ad = DialogBuilder.buildLoadingDialog(requireContext(),R.layout.uploading_view)
        }

        binding.frSettingDownloadLy.setOnClickListener {
            vm.readEntireDatabase(localClientFile,localMaintainFile,localDieselFile)
            ad = DialogBuilder.buildLoadingDialog(requireContext(),R.layout.downloading_view)
        }


    }

    private fun collectLoadingEvent(){
       viewLifecycleOwner.lifecycleScope.launch {
           viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

               vm.loadingEventFlow.collect {
                   when(it) {
                       LoadingEvent.Downloading,LoadingEvent.Uploading -> {
                           ad.show()
                       }
                       LoadingEvent.Downloaded,LoadingEvent.Uploaded -> {
                           ad.dismiss()
                       }

                   }
               }

           }
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
            vm.setRvPrice(updatedFee)
        }

        showBs(listener)

    }

    private fun showHrBs(){
        val listener = {updatedFee:Int ->

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
            binding.frSettingThemeImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.night))
        else
            binding.frSettingThemeImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.light))
    }

    private fun setLangImage(isEnglish:Boolean){
        if (isEnglish)
            binding.frSettingLangImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.english))
        else
            binding.frSettingLangImg.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.myanmar))


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



    private fun generateFile(context: Context, fileName: String): File {

        val csvFile = File(context.getExternalFilesDir(null), fileName)
        csvFile.createNewFile()//create new file only if a file with that name doesn't yet exist

        return csvFile
    }










}