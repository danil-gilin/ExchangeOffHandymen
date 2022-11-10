package com.example.exchangeofhandymen.presenter.home.profile.skillAdd

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.AddSkillUseCase
import com.example.exchangeofhandymen.domain.GetAllSkillsUseCase
import com.example.exchangeofhandymen.entity.CustomException
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SkillsAddViewModel @Inject constructor(
    private val getAllSkillsUseCase: GetAllSkillsUseCase,
    private val addSkillUseCase: AddSkillUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<StateSkillsAdd>(StateSkillsAdd.Loading)
    val state = _state.asStateFlow()


    fun getAllSkills() {
        viewModelScope.launch {
            _state.value = StateSkillsAdd.Loading
            try {
                val allSkills = getAllSkillsUseCase.getAllSkills()
                _state.value = StateSkillsAdd.Success(allSkills)
            } catch (e: Exception) {
                Log.d("AllSkills", e.message.toString())
                _state.value = StateSkillsAdd.Error
            }
        }
    }

    fun addSkill(
        skill: Skill,
        allskill: List<Skill>?,
        skillUser: List<String>,
        deleteSkill: ArrayList<Skill>,
        user: User
    ) {
        viewModelScope.launch {
            _state.value = StateSkillsAdd.Loading
            var deleteSkillTemp = deleteSkill
            try {
                if (skill.name !in skillUser) {
                    if (skill.name in (allskill?.map { it.name }?.minus(skillUser)
                            ?: emptyList())
                    ) {
                        val x = allskill?.find { skill.name == it.name }
                        if (x != null) {
                            x.let { addSkillUseCase.addOldSkill(it.id) }
                            if (x.id in deleteSkillTemp.map { it.id }) {
                                deleteSkillTemp =
                                    deleteSkillTemp.filter { it.id != x.id } as ArrayList<Skill>
                                val newUser = User(
                                    user.name,
                                    user.phone,
                                    user.email,
                                    user.rating,
                                    user.geoPoint,
                                    user!!.skills?.minus(deleteSkillTemp.map { it.id }.toSet()),
                                    user.description,
                                    user.wokerFlag,
                                    user.img
                                )
                                _state.value = StateSkillsAdd.SuccessSave(newUser)
                            } else {
                                val newUser = User(
                                    user.name,
                                    user.phone,
                                    user.email,
                                    user.rating,
                                    user.geoPoint,
                                    user!!.skills?.plus(x.id)
                                        ?.minus(deleteSkillTemp.map { it.id }.toSet()),
                                    user.description,
                                    user.wokerFlag,
                                    user.img
                                )
                                _state.value = StateSkillsAdd.SuccessSave(newUser)
                            }
                        }
                    } else {

                        val idSkill = addSkillUseCase.addNewSkill(skill)

                        if (idSkill in deleteSkillTemp.map { it.id }) {
                            deleteSkillTemp =
                                deleteSkillTemp.filter { it.id != idSkill } as ArrayList<Skill>
                            val newUser = User(
                                user.name,
                                user.phone,
                                user.email,
                                user.rating,
                                user.geoPoint,
                                user!!.skills?.minus(deleteSkillTemp.map { it.id }.toSet()),
                                user.description,
                                user.wokerFlag,
                                user.img
                            )
                            _state.value = StateSkillsAdd.SuccessSave(newUser)
                        } else {
                            val newUser = User(
                                user.name,
                                user.phone,
                                user.email,
                                user.rating,
                                user.geoPoint,
                                user!!.skills?.plus(idSkill)
                                    ?.minus(deleteSkillTemp.map { it.id }.toSet()),
                                user.description,
                                user.wokerFlag,
                                user.img
                            )
                            _state.value = StateSkillsAdd.SuccessSave(newUser)
                        }
                    }
                } else {
                    throw CustomException("У вас уже есть этот навык")
                }
            } catch (e: Exception) {
                throw CustomException("Не полчилось сохранить ваш навык. Попробуйте позже")
            }

        }
    }
}