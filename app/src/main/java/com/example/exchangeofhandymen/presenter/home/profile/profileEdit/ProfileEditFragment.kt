package com.example.exchangeofhandymen.presenter.home.profile.profileEdit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentProfileEditBinding
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.profileAdapter.SkillsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileEditFragment()
    }


    @Inject
    lateinit var profileEditFactory: ProfileEditFactory

    private val viewModel: ProfileEditViewModel by viewModels{profileEditFactory}
    private lateinit var binding:FragmentProfileEditBinding
    private var user: User?=null
    private  var skillsNameUser:List<String>?=null
    private val adapter= SkillsAdapter({addSkill()},{skill->deleteSkill(skill)})
    private var deleteSkill= arrayListOf<Skill>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileEditBinding.inflate(inflater)
        binding.skillsEdit.adapter=adapter
        arguments.let {
            if(it !=null){
                user=it.getParcelable("user")
            }
        }

        init()
        initUser()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
          viewModel.state.collect{
              when(it){
                  is  ProfEditState.Error -> {
                      binding.progressBarEdit.visibility=View.GONE
                      binding.descriptionEditLayout.error=it.errorDescription
                      binding.mailEditLayout.error=it.errorEmail
                      binding.nameEditLayout.error=it.errorName
                  }
                  ProfEditState.Loading -> {
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                    binding.progressBarEdit.visibility=View.VISIBLE
                  }
                  ProfEditState.Start -> {
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                      binding.progressBarEdit.visibility=View.GONE
                  }
                  is ProfEditState.Success -> {
                      binding.progressBarEdit.visibility=View.GONE
                      skillsNameUser= it.skills?.map { it.name } as ArrayList<String>?
                      adapter.submitList(it.skills)
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                  }
                  ProfEditState.SuccessSave -> {
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                      binding.progressBarEdit.visibility=View.GONE
                      findNavController().popBackStack()
                  }
              }
          }
        }


        binding.btnSave.setOnClickListener{
            if(user!=null) {
                val decriptionEdit=binding.descriptionEdit.text.toString()
                val nameEdit=binding.nameEdit.text.toString()
                val emailEdit=binding.mailEdit.text.toString()
                val editUser= User(nameEdit,user!!.phone,emailEdit,user!!.rating,user!!.geoPoint,user!!.skills,decriptionEdit)
                viewModel.deleteUserSkill(deleteSkill.map { it.id } as ArrayList<String>)
                viewModel.editUser(editUser)
            }
        }

        binding.exitEdit.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }


    override fun onResume() {
        super.onResume()
        initUser()
    }

    private fun initUser() {
        if(user!=null) {
            binding.descriptionEdit.setText(user!!.description)
            binding.mailEdit.setText(user!!.email)
            binding.nameEdit.setText(user!!.name)
            viewModel.gettSkills(user!!.skills)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){
        binding.descriptionEdit.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }
    }

  private fun addSkill(){
        val bundle=Bundle()
        skillsNameUser= skillsNameUser?.minus(deleteSkill.map { it.name }.toSet())
      if(skillsNameUser?.size !=1){
          bundle.putStringArrayList("SkillsUser", skillsNameUser as ArrayList<String>)
      }
        bundle.putParcelable("user",user)
        bundle.putParcelableArrayList("deleteSkill",deleteSkill)
       findNavController().navigate(R.id.action_profileEditFragment_to_skillsAddFragment,bundle)
    }

    private fun deleteSkill(skill: Skill){
        deleteSkill.add(skill)
      adapter.submitList( adapter.currentList.minus(skill))
    }


}