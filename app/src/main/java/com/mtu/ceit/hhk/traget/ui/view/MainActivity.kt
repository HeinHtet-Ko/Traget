package com.mtu.ceit.hhk.traget.ui.view


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.ActivityMainBinding
import com.mtu.ceit.hhk.traget.ui.viewmodel.ClientViewModel
import com.mtu.ceit.hhk.traget.ui.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
     val clientVM: ClientViewModel by viewModels()
     val settingVM:SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeIsNight()
        setUpBottomView()


    }
    private fun setUpBottomView(){
        val nh = supportFragmentManager.findFragmentById(R.id.main_Container) as NavHostFragment
        val con = nh.navController

        NavigationUI.setupWithNavController(binding.mainBottom,con)
    }
    private fun observeIsNight(){
        settingVM.nightFlow.observe(this){
            if(it)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}