package com.example.exchangeofhandymen.presenter.home.bag.jobInfoFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exchangeofhandymen.databinding.FragmentJobInfoBinding
import com.example.exchangeofhandymen.entity.job.JobWithId
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JobInfoFragment : Fragment() {

    companion object {
        fun newInstance() = JobInfoFragment()
    }

    @Inject
    lateinit var factory: JobInfoFactory



    private val viewModel: JobInfoViewModel by viewModels { factory }
    private lateinit var binding:FragmentJobInfoBinding
    private var adapaterWorker= WorkerJobInfoAdater()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentJobInfoBinding.inflate(inflater)
        arguments.let {
            if (it != null) {
                val job:JobWithId= it.getParcelable("Job")!!
                initJob(job)
            }
        }

        binding.rcJobInfo.adapter=adapaterWorker

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.listWorker.collect {
                if(it.isEmpty()){
                    binding.txtWorkersInfoJob.isVisible=false
                }else{
                    binding.txtWorkersInfoJob.isVisible=true
                    adapaterWorker.submitList(it)
                }
            }
        }



        binding.btnBackInfoJob.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun initJob(job: JobWithId) {
        binding.countMoneyInfo.text=job.money
        binding.nameInfoJob.text=job.name
        binding.counterWorkersInfo.text="${job.workerAccept.size}/${job.countWorkers}"
        binding.descriptionInfoJob.text=job.info
        binding.geoJobInfo.text=job.geopoint?.getTownName(activity!!)

        binding.viewPagerImg.adapter=ImgInfoJobAdapter(job.img)
        viewModel.getListAcceptJob(job.workerAccept)
    }
}