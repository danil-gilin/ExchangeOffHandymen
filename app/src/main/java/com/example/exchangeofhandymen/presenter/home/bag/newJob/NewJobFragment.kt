package com.example.exchangeofhandymen.presenter.home.bag.newJob

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentNewJobBinding
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.job.Job
import com.example.exchangeofhandymen.presenter.home.profile.profileEdit.ProfileEditFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class NewJobFragment : Fragment() {

    companion object {
        fun newInstance() = NewJobFragment()
        private const val GET_PHOTO="image/*"
        private val REQUIRED_PERMISSONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }


    @Inject
    lateinit var factory:NewJobFactory

    private val auth=FirebaseAuth.getInstance()
    private lateinit var binding:FragmentNewJobBinding
    private val viewModel: NewJobViewModel by viewModels{ factory }
    private var geoPosition: GeoPosition?=null
    private  var imgList= arrayListOf<Uri>()
    private lateinit var fusedClient: FusedLocationProviderClient

    private val launcer=registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted->
    }
    private  val pickMedia=registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            Log.d("SaveJob",imgList.size.toString())
            when(imgList.size){
                0->{
                    Glide.with(requireContext()).load(uri).centerCrop().into(binding.firstImgJob)
                    imgList.add(uri)
                }
                1->{
                    Glide.with(requireContext()).load(uri).centerCrop().into(binding.secondImgJob)
                    imgList.add(uri)
                }
                2->{
                    Glide.with(requireContext()).load(uri).centerCrop().into(binding.thirdImgJob)
                    imgList.add(uri)
                }
                else ->{}
            }
        } else {
        }
    }
    private val launcerGeo=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            map->
        if(map.values.isNotEmpty() && map.values.all { it }){
            startLocation()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentNewJobBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect{
                when(it){
                    NewJobState.Error -> {
                        binding.progressBar.isVisible=false
                    }
                    NewJobState.Loading -> {
                        binding.progressBar.isVisible=true
                    }
                    NewJobState.Start -> {
                        binding.progressBar.isVisible=false
                        initView()
                    }
                    NewJobState.Success -> {
                        binding.progressBar.isVisible=false
                        findNavController().popBackStack()
                    }
                    is NewJobState.SuccessSaveGeo -> {
                        binding.progressBar.isVisible=false
                        geoPosition= GeoPosition(it.latitude,it.longitude)
                        binding.txtGeoPositionJob.text= geoPosition!!.getTownName(activity!!)

                    }
                }
            }
        }




        binding.btnSaveJob.setOnClickListener {
            val employer=auth.currentUser?.uid.toString()
            val money=binding.countMoney.getCount()
            val workers=binding.counterWorkers.getCount()
            val info=binding.InfoWorkEdit.text.toString()
            val name=binding.jobNameEdit.text.toString()
            val jobImgList=imgList.map { it.toString() }
            val job=Job(employer, emptyList<String>(),geoPosition,jobImgList,money,workers,info,name,auth.currentUser?.phoneNumber.toString(),emptyList<String>())
            viewModel.saveJob(job)
        }


        binding.firstImgJob.setOnClickListener {
            checkPermission()
        }
        binding.secondImgJob.setOnClickListener {
            checkPermission()
        }
        binding.thirdImgJob.setOnClickListener {
            checkPermission()
        }

        binding.btnGeoJob.setOnClickListener {
            checkGeoPermission()
        }

        binding.btnBackNewJob.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.countMoney.changeMaxCount(100000)
        binding.countMoney.changeMinCount(100)
        binding.countMoney.changeStepCount(100)

        binding.counterWorkers.changeMaxCount(20)
        binding.counterWorkers.changeMinCount(1)
        binding.counterWorkers.changeStepCount(1)

        binding.InfoWorkEdit.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

    }

    private fun checkPermission(){
        val allGranted=
            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
        if(!allGranted){
            launcer.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }else{
            pickMedia.launch(GET_PHOTO)
        }
    }

    private fun checkGeoPermission(){
        if(REQUIRED_PERMISSONS.all { permission->
                ContextCompat.checkSelfPermission(requireContext(),permission)== PackageManager.PERMISSION_GRANTED
            }){
        }else{
            launcerGeo.launch(REQUIRED_PERMISSONS)
        }
        startLocation()
    }
    private fun startLocation(){
        fusedClient= LocationServices.getFusedLocationProviderClient(requireContext())
        viewModel.saveGeo(fusedClient,REQUIRED_PERMISSONS)
    }

}