package com.example.exchangeofhandymen.presenter.home.workers.workerProfile

import android.location.Address
import android.location.Geocoder
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
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentWorkerProfileBinding
import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.entity.worker.WorrkerInt
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.profileAdapter.SkillsAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WorkerProfileFragment : Fragment() {

    companion object {
        fun newInstance() = WorkerProfileFragment()
    }

    @Inject
    lateinit var factory:WorkerProfileFactory

    private lateinit var binding:FragmentWorkerProfileBinding
    private val viewModel: WorkerProfileViewModel by viewModels{factory}
    private lateinit var worker: WorrkerInt
    private val adapter= SkillsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWorkerProfileBinding.inflate(inflater)
        arguments.let {
            if(it !=null){
                worker= it.getParcelable("worker")!!
                initUser()
            }
        }

        viewModel.getIsWorker()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.isWorker.collect{
                Log.d("isWorker",it.toString())
                binding.btnInvite.isVisible = !it
            }
        }


        binding.btnInvite.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("workerUid",worker.id)

            findNavController().navigate(R.id.action_workerProfileFragment_to_jobSelecetFragment,bundle)
        }

        binding.bactWorkerBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun initUser(){

        if(worker.img.isNotEmpty()) {
            Glide.with(requireContext()).load(worker.img).circleCrop()
                .into(binding.imgUserAvatarWorker)
        }else{
            Glide.with(requireContext()).load(R.drawable.worker_icon).circleCrop()
                .into(binding.imgUserAvatarWorker)
        }
        with(binding){
            txtNameUserWorker.text=worker.name
            txtPhoneUserWorker.text=worker.phone
            rcSkillsWorker.layoutManager= FlexboxLayoutManager(requireContext())
            rcSkillsWorker.adapter=adapter
            adapter.submitList(worker.skills)
            if(worker.geoPoint!=null){
                txtGeoUserWorker.text=getTownGeo(worker.geoPoint!!.latitude, worker.geoPoint!!.longitude)
            }
            txtDescriptionUserWorker.text=worker.description
            txtMailUserWorker.text=worker.email
            ratingUserWorker.rating= worker.rating?.toFloat()!!
        }
    }

    private fun getTownGeo(lat: Double, lon: Double): String {
        try {
           val geocoder = Geocoder(requireContext(), Locale("Ru"))
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            return addresses.get(0).locality
        } catch (e: Exception) {
            return ""
        }
    }


}