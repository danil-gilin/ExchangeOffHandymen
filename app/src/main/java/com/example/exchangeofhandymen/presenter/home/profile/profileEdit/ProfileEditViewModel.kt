package com.example.exchangeofhandymen.presenter.home.profile.profileEdit

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.DeleteSkillUseCase
import com.example.exchangeofhandymen.domain.EditUserUseCase
import com.example.exchangeofhandymen.domain.GettSkillsUseCase
import com.example.exchangeofhandymen.entity.CustomException
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val gettSkillsUseCase: GettSkillsUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val deleteSkillUseCase: DeleteSkillUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfEditState>(ProfEditState.Start)
    val state = _state.asStateFlow()

    fun gettSkills(skillsId: List<String>?) {
        viewModelScope.launch {
            _state.value = ProfEditState.Loading
            if (skillsId != null) {
                var listSkills = gettSkillsUseCase.getSkills(skillsId)
                listSkills = listSkills.plus(Skill("my", "+", 0))
                _state.value = ProfEditState.Success(listSkills)
            } else {
                val listSkills = listOf<Skill>(Skill("my", "+", 0))
                _state.value = ProfEditState.Success(listSkills)
            }

        }
    }

    fun editUser(user: User) {
        viewModelScope.launch {
            _state.value = ProfEditState.Loading
            var errorName: String? = null
            var errorEmail: String? = null
            var errorDescription: String? = null
            var flagNotError = true
            try {
                if (user.name?.isEmpty() == true){
                    errorName="Имя не может быть пусты"
                    flagNotError = false
                }
                if (user.email?.isEmpty() == true){
                    errorEmail="Почта не может быть пустой"
                    flagNotError = false
                }
                if(!isEmailValid(user.email.toString())){
                    errorEmail="Почта введена неверно"
                    flagNotError = false
                }
                if (user.description?.isEmpty() == true){
                    errorDescription="Расскажите о себе"
                    flagNotError = false
                }
                if (flagNotError) {
                    editUserUseCase.editProfile(user)
                    _state.value = ProfEditState.SuccessSave
                } else {
                    throw CustomException("Ошибка редактирования")
                }
            } catch (e: Exception) {
                _state.value = ProfEditState.Error(errorName, errorEmail, errorDescription)
            }
        }
    }

    fun deleteUserSkill(skillId: ArrayList<String>) {
        viewModelScope.launch {
            _state.value = ProfEditState.Loading
            try {
                deleteSkillUseCase.deleteSkill(skillId)
            } catch (e: Exception) {

            }
        }
    }

   private fun isEmailValid(email:String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}