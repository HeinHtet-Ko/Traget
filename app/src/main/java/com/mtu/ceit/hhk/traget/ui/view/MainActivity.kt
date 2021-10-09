package com.mtu.ceit.hhk.traget.ui.view


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mtu.ceit.hhk.traget.R
import com.mtu.ceit.hhk.traget.databinding.ActivityMainBinding
import com.mtu.ceit.hhk.traget.ui.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
     val clientVM: ClientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomView()

    }
    private fun setUpBottomView(){
        val nh = supportFragmentManager.findFragmentById(R.id.main_Container) as NavHostFragment
        val con = nh.navController

        NavigationUI.setupWithNavController(binding.mainBottom,con)
    }
}