package com.example.exchangeofhandymen.presenter.home.workers

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentWorkersBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkersFragment : Fragment() {

    companion object {
        fun newInstance() = WorkersFragment()
    }

    @Inject
    lateinit var workersViewModelFactory: WorkersViewModelFactory


    private lateinit var binding:FragmentWorkersBinding
    private val viewModel: WorkersViewModel by viewModels{  workersViewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWorkersBinding.inflate(inflater)




        return binding.root
    }


}