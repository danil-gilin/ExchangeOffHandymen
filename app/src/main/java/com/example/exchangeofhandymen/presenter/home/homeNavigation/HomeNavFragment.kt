package com.example.exchangeofhandymen.presenter.home.homeNavigation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
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
    private val auth=FirebaseAuth.getInstance()
    private lateinit var phoneNumber:String
    private var newProfile=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeNavBinding.inflate(inflater)



        arguments.let {
            phoneNumber = it?.getString("phoneNumber").toString()
            newProfile= it?.getBoolean("newUser") == true
        }


        val navView: BottomNavigationView = binding.navView

        val navHostFragment =  childFragmentManager.findFragmentById(R.id.nav_host_fragment_home_content_nav) as NavHostFragment
        val navController = navHostFragment.navController
        Log.d("userMy","1 ${auth.currentUser}")
        if(newProfile){
            viewModel.checkUser(auth.currentUser,phoneNumber)
        }


        navView.setupWithNavController(navController)

        return binding.root
    }


}