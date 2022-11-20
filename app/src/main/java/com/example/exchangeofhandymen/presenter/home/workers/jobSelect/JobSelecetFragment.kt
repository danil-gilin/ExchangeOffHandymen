package com.example.exchangeofhandymen.presenter.home.workers.jobSelect

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentJobSelecetBinding
import com.example.exchangeofhandymen.entity.job.JobWithId
import com.example.exchangeofhandymen.presenter.home.bag.listJob.adapterJob.AdapterJob
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class JobSelecetFragment : Fragment() {

    companion object {
        fun newInstance() = JobSelecetFragment()
    }
    @Inject
    lateinit var factory:JobSelectFactory


    private lateinit var binding:FragmentJobSelecetBinding
    private val auth=FirebaseAuth.getInstance()
    private val viewModel: JobSelecetViewModel by viewModels { factory }
    private val adapter=AdapterJob(clickselect = {job->selectJob(job)})
    private var workerUid:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentJobSelecetBinding.inflate(inflater)
        binding.rcSelectJob.layoutManager=LinearLayoutManager(requireContext())
        binding.rcSelectJob.adapter=adapter

        arguments.let {
            if (it != null) {
                workerUid= it.getString("workerUid").toString()
            }
        }


        viewModel.getJobEmployer(auth.currentUser?.uid.toString(),workerUid)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect{
             when(it){
                  is JobSelectState.Error -> {
                    binding.layuotErrorSelect.isVisible=true
                      binding.errorPreviewSelect.text=it.message
                 }
                 JobSelectState.Loading -> {
                     binding.layuotErrorSelect.isVisible=false
                 }
                 is JobSelectState.Success -> {
                     binding.layuotErrorSelect.isVisible=false
                     adapter.submitList(it.jobs)
                 }
                 JobSelectState.SuccessSelect -> {
                     binding.layuotErrorSelect.isVisible=false
                     findNavController().popBackStack()
                 }
             }
            }
        }


        binding.closeExitSelect.setOnClickListener {
            findNavController().popBackStack()
        }



        return binding.root
    }

    private fun selectJob(job:JobWithId){
        viewModel.invite(job.id,workerUid)
    }

}