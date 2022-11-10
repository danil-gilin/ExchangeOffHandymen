package com.example.exchangeofhandymen.presenter.home.homeNavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentHomeNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeNavFragment : Fragment() {

    companion object {
        fun newInstance() = HomeNavFragment()
    }

    @Inject
    lateinit var homeNavViewModelFactory: HomeNavViewModelFactory

    private lateinit var binding: FragmentHomeNavBinding
    private val viewModel: HomeNavViewModel by viewModels { homeNavViewModelFactory }
    private val auth = FirebaseAuth.getInstance()
    private lateinit var phoneNumber: String
    private var newProfile = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeNavBinding.inflate(inflater)


        arguments.let {
            phoneNumber = it?.getString("phoneNumber").toString()
            newProfile = it?.getBoolean("newUser") == true
        }

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_home_content_nav) as NavHostFragment
        val navController = navHostFragment.navController


        if (newProfile) {
            var worker = true
            val myDialogFragment = WorkerDialogFragment()
            val manager = childFragmentManager
            myDialogFragment.show(manager, "myDialog")
            childFragmentManager.setFragmentResultListener("Dialog_rezult", this) { key, bundle ->
                worker = bundle.getBoolean("dialog_key")
            }
            viewModel.checkUser(auth.currentUser, phoneNumber, worker)
        }


        navView.setupWithNavController(navController)


        return binding.root
    }


}