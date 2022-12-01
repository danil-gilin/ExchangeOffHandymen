package com.example.exchangeofhandymen.presenter.home.workers.workersList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentWorkersBinding
import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.entity.worker.WorkerDB
import com.example.exchangeofhandymen.entity.worker.WorrkerInt
import com.example.exchangeofhandymen.presenter.home.workers.workersList.adapterWorkers.WorkersAdapter
import com.google.firebase.auth.FirebaseAuth

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkersFragment : Fragment() {

    companion object {
        fun newInstance() = WorkersFragment()
    }

    @Inject
    lateinit var workersViewModelFactory: WorkersViewModelFactory


    private var auth=FirebaseAuth.getInstance()
    private lateinit var binding:FragmentWorkersBinding
    private lateinit var adapter:WorkersAdapter
    private val viewModel: WorkersViewModel by viewModels{  workersViewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWorkersBinding.inflate(inflater)
        adapter= WorkersAdapter(){worker ->clickWorker(worker)}
        binding.workersRc.adapter=adapter

        viewModel.getListWorkerGeo(auth.currentUser?.uid.toString())

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect{
                when(it){
                   is WorkersState.Error ->{
                       binding.layuotError.isVisible=true
                       binding.errorTxt.text=it.message
                       binding.errorPreview.text=it.preview
                        binding.progressWorkers.isVisible=false
                        binding.layuotError.isVisible=true
                    }
                    WorkersState.Loading -> {
                        binding.layuotError.isVisible=false
                        binding.progressWorkers.isVisible=true
                        binding.layuotError.isVisible=false
                    }
                    WorkersState.Start -> {
                        binding.layuotError.isVisible=false
                        binding.progressWorkers.isVisible=false
                        binding.layuotError.isVisible=false
                    }
                    is WorkersState.Success ->{
                        binding.layuotError.isVisible=false
                        binding.progressWorkers.isVisible=false
                        binding.layuotError.isVisible=false
                        adapter.submitList(it.worker)
                    }
                }
            }
        }



        return binding.root
    }

    private fun clickWorker(worker: WorrkerInt){
        val bundle=Bundle()
        if (worker is Worker) {
            bundle.putParcelable("worker", worker)
        }
        if(worker is WorkerDB){
            bundle.putParcelable("worker", worker)
        }
        findNavController().navigate(R.id.action_navigation_workers_to_workerProfileFragment,bundle)
    }


}