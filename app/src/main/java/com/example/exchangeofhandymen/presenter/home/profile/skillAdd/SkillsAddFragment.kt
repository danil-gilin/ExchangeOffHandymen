package com.example.exchangeofhandymen.presenter.home.profile.skillAdd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.exchangeofhandymen.databinding.FragmentSkillsAddBinding
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SkillsAddFragment : Fragment() {

    companion object {
        fun newInstance() = SkillsAddFragment()
    }

    @Inject
    lateinit var skillsAddfactory: SkillsAddViewModelFactory

    private val viewModel: SkillsAddViewModel by viewModels { skillsAddfactory }
    private lateinit var skillUser: List<String>
    private lateinit var binding: FragmentSkillsAddBinding
    private var allskill: List<Skill>? = null
    private lateinit var user: User
    private var deleteSkill = arrayListOf<Skill>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSkillsAddBinding.inflate(inflater)
        arguments.let {
            if (it != null) {
                if(it.getStringArrayList("SkillsUser")!=null){
                    skillUser = it.getStringArrayList("SkillsUser")!!
                }else{
                    skillUser= emptyList()
                }
                user = it.getParcelable("user")!!
                deleteSkill = it.getParcelableArrayList("deleteSkill")!!
            }
        }
        viewModel.getAllSkills()


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                when (state) {
                    StateSkillsAdd.Error -> {}
                    StateSkillsAdd.Loading -> {}
                    is StateSkillsAdd.SuccessSave -> {
                        val bundle = Bundle()

                        bundle.putParcelable("user", state.newUser)
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("addFragment", bundle)
                        findNavController().popBackStack()
                    }
                    is StateSkillsAdd.Success -> {
                        val sortSkills = state.skills?.sortedBy { it.population }?.reversed()
                        allskill = sortSkills
                        var nameSkills = sortSkills!!.map { it.name }
                        nameSkills = nameSkills.minus(skillUser)
                        var adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            nameSkills
                        )
                        binding.skillsEditText.threshold = 0
                        binding.skillsEditText.setAdapter(adapter)
                        binding.skillsEditText.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.skillsEditText.showDropDown() }
                    }
                }
            }
        }


        binding.btnAddSkill.setOnClickListener {
            val skill = Skill("", binding.skillsEditText.text.toString(), 0)
            if(skill.name !in deleteSkill.map { it.name }){
                viewModel.addSkill(skill, allskill, skillUser,deleteSkill,user)
            }else{
                val bundle = Bundle()
                bundle.putParcelable("user", user)
                bundle.putString("skill", skill.name)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("addFragment", bundle)
                findNavController().popBackStack()
            }
        }

        binding.btnBackSkillAdd.setOnClickListener {
            val bundle = Bundle()

            bundle.putParcelable("user", user)

            findNavController().previousBackStackEntry?.savedStateHandle?.set("addFragment", bundle)
            findNavController().popBackStack()
        }

        return binding.root
    }


}