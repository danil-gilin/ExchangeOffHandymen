package com.example.exchangeofhandymen.presenter.home.profile.profileUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.GetProfileUseCase
import com.example.exchangeofhandymen.domain.GettSkillsUseCase
import com.example.exchangeofhandymen.entity.Skill
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val getProfileUseCase: GetProfileUseCase,private val gettSkillsUseCase: GettSkillsUseCase): ViewModel() {
    private val _state= MutableStateFlow<StateProfile>(StateProfile.Loading)
     val state=_state.asStateFlow()




    fun getUser(currentUser: FirebaseUser?) {
        viewModelScope.launch {
            if (currentUser != null) {
               val user= getProfileUseCase.getProfile(currentUser.uid)
                var skills= emptyList<Skill>()
                if (user.skills !=null) {
                    skills = gettSkillsUseCase.getSkills(user.skills)
                }
                _state.value= StateProfile.Success(
                    user,
                    skills.sortedBy { it.population }.reversed()
                )
            }else{
                _state.value= StateProfile.Error
            }
        }
    }

}