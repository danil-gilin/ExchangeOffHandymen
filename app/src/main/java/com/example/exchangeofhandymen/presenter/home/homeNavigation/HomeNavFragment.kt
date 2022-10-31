package com.example.exchangeofhandymen.presenter.home.homeNavigation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentHomeNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeNavFragment : Fragment() {

    companion object {
        fun newInstance() = HomeNavFragment()
    }

    @Inject
    lateinit var homeNavViewModelFactory: HomeNavViewModelFactory

    private lateinit var binding:FragmentHomeNavBinding
    private val viewModel: HomeNavViewModel by viewModels{homeNavViewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeNavBinding.inflate(inflater)


        val navView: BottomNavigationView = binding.navView

        val navHostFragment =  childFragmentManager.findFragmentById(R.id.nav_host_fragment_home_content_nav) as NavHostFragment
        val navController = navHostFragment.navController



        navView.setupWithNavController(navController)

        return binding.root
    }


}