package com.example.exchangeofhandymen.presenter.home.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory


    private lateinit var binding:FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater)





        return binding.root
    }



}