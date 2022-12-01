package com.example.exchangeofhandymen.presenter.home.profile.profileUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentProfileBinding
import com.example.exchangeofhandymen.entity.User.User
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.profileAdapter.SkillsAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.auth.FirebaseAuth

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
    private val auth= FirebaseAuth.getInstance()
    private val adapter= SkillsAdapter()
    private var user: User?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding= FragmentProfileBinding.inflate(inflater)

        val layoutManager=FlexboxLayoutManager(requireContext())

        binding.rcSkills.layoutManager=layoutManager
        binding.rcSkills.adapter=adapter
        viewModel.getUser(auth.currentUser)

        binding.btnEdit.setOnClickListener {
            val bundle=Bundle()
            bundle.putParcelable("user",user)
            findNavController().navigate(R.id.action_navigation_profile_to_profileEditFragment,bundle)
        }


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect{
                when(it){
                    StateProfile.Loading ->{
                      binding.progressProfile.visibility=View.VISIBLE
                        binding.btnEdit.isEnabled=false
                    }
                    StateProfile.Start -> {
                        binding.progressProfile.visibility=View.GONE
                    }
                    is StateProfile.Success -> {
                        binding.progressProfile.visibility=View.GONE
                        user=it.user
                        binding.txtNameUser.text=it.user.name
                        if(it.user.rating?.toFloat()!=null){
                            binding.ratingUser.rating= it.user.rating?.toFloat()!!
                        }
                        if(user?.wokerFlag == true){
                            if(user!!.img.isEmpty()){
                                Glide.with( binding.imgUserAvatar).load(R.drawable.worker_icon).circleCrop().into( binding.imgUserAvatar)
                            }else{
                                Glide.with( binding.imgUserAvatar).load(user!!.img).circleCrop().into( binding.imgUserAvatar)
                            }
                        }else{
                            if(user!!.img.isEmpty()){
                                Glide.with( binding.imgUserAvatar).load(R.drawable.employer_icon).circleCrop().into( binding.imgUserAvatar)
                            }else{
                                Glide.with( binding.imgUserAvatar).load(user!!.img).circleCrop().into( binding.imgUserAvatar)
                            }
                        }
                        binding.imgUserAvatar
                        adapter.submitList(it.skills)
                        binding.txtPhoneUser.text=it.user.phone
                        binding.txtDescriptionUser.text=it.user.description
                        binding.txtMailUser.text=it.user.email
                        if(it.user.geoPoint!=null){
                            binding.txtGeoUser.text=user!!.geoPoint!!.getTownName(requireContext())
                        }

                        binding.btnEdit.isEnabled=true
                    }
                    StateProfile.Error -> {
                        binding.progressProfile.visibility=View.GONE
                    }
                }
            }
        }


        return binding.root
    }
}