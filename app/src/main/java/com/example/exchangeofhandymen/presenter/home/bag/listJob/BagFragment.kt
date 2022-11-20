package com.example.exchangeofhandymen.presenter.home.bag.listJob

import android.os.Bundle
import android.util.Log
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
import com.example.exchangeofhandymen.databinding.FragmentBagBinding
import com.example.exchangeofhandymen.entity.job.JobWithId
import com.example.exchangeofhandymen.presenter.home.bag.listJob.adapterJob.AdapterJob
import com.example.exchangeofhandymen.presenter.home.bag.listJob.adapterJob.AdapterWorkerJob
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BagFragment : Fragment() {

    companion object {
        fun newInstance() = BagFragment()
    }

    @Inject
    lateinit var factory: BagFactory

    private lateinit var binding:FragmentBagBinding
    private val viewModel: BagViewModel by viewModels{ factory }
    private val adapter=AdapterJob(clickdelte ={job->deleteJob(job)}, clickInfo = {job->infoJob(job)})
    private val adapterWorkerJob=AdapterWorkerJob({job->deleteWorkerJob(job)},{job->infoJob(job)},{job->selectWorkerJob(job)})
    private var listJob= listOf<JobWithId>()
    private var isWorker=true
    private val auth=FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBagBinding.inflate(inflater)
        binding.rcJob.layoutManager=LinearLayoutManager(requireContext())

        viewModel.isWorker(auth.currentUser?.uid.toString())

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
           viewModel.state.collect{
               when (it) {
                   is BagState.Error -> {
                       binding.layuotErrorBag.isVisible=true
                        binding.errorPreviewBag.text=it.error
                       if(isWorker){
                           adapterWorkerJob.submitList(emptyList())
                       }else{
                           adapter.submitList(emptyList())
                       }
                   }
                   BagState.Loading -> {
                       binding.layuotErrorBag.isVisible=false
                   }
                   is BagState.Success -> {
                       binding.layuotErrorBag.isVisible=false
                       if(isWorker){
                           adapterWorkerJob.submitList(it.jobs)
                       }else{
                           adapter.submitList(it.jobs)
                       }
                       listJob=it.jobs
                   }
                   is BagState.Start ->{
                       binding.layuotErrorBag.isVisible=false
                       isWorker=it.isWorker
                       if (it.isWorker){
                           viewModel.getJobForWork(auth.currentUser?.uid.toString())
                           binding.rcJob.adapter=adapterWorkerJob
                           binding.addJob.isVisible=false
                       }else{
                           viewModel.getJobEmployer(auth.currentUser?.uid.toString())
                           binding.rcJob.adapter=adapter
                           binding.addJob.isVisible=true
                       }
                   }
               }
           }
        }

        binding.addJob.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_bag_to_newJobFragment)
        }

        return binding.root
    }

    private fun deleteJob(job: JobWithId){
        viewModel.deleteJob(job.id,listJob,job.img)
    }

    private fun deleteWorkerJob(job: JobWithId){
        viewModel.deleteWorkerJob(job.id,auth.currentUser?.uid.toString(),listJob)
    }

    private fun selectWorkerJob(job: JobWithId){
        viewModel.selectWorkerJob(job.id,auth.currentUser?.uid.toString(),listJob)
    }

    private fun infoJob(job: JobWithId) {
        val bundle=Bundle()
        bundle.putParcelable("Job",job)
        findNavController().navigate(R.id.action_navigation_bag_to_jobInfoFragment,bundle)
    }
}