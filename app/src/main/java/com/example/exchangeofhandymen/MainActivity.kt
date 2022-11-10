package com.example.exchangeofhandymen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.exchangeofhandymen.presenter.logInSigIn.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var navHostFragment= supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_nav) as NavHostFragment
        var navController = navHostFragment.navController

    }
}