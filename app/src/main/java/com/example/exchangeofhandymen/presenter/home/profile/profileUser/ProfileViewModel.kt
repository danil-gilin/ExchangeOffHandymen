package com.example.exchangeofhandymen.presenter.home.profile.profileUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.profile.GetProfileUseCase
import com.example.exchangeofhandymen.domain.skill.GettSkillsUseCase
import com.example.exchangeofhandymen.entity.Constance
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val gettSkillsUseCase: GettSkillsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<StateProfile>(StateProfile.Loading)
    val state = _state.asStateFlow()


    fun getUser(currentUser: FirebaseUser?) {
        viewModelScope.launch {
            if (currentUser != null) {
                var user = getProfileUseCase.getProfile(currentUser.uid)
                var skills = emptyList<Skill>()
                if (user.skills != null) {
                    skills = gettSkillsUseCase.getSkills(user.skills!!)
                }

                if (user.email?.isEmpty() == true) {
                    user = User(
                        user.name,
                        user.phone,
                        Constance.NoInformationText,
                        user.rating,
                        user.geoPoint,
                        user.skills,
                        user.description,
                        user.wokerFlag,
                        user.img
                    )
                }
                if (user.description?.isEmpty() == true) {
                    user = User(
                        user.name,
                        user.phone,
                        user.email,
                        user.rating,
                        user.geoPoint,
                        user.skills,
                        Constance.NoInformationText,
                        user.wokerFlag,
                        user.img
                    )
                }
                _state.value = StateProfile.Success(
                    user,
                    skills.sortedBy { it.population }.reversed()
                )
            } else {
                _state.value = StateProfile.Error
            }
        }
    }

}