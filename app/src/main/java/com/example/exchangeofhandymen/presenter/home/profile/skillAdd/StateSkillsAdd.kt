package com.example.exchangeofhandymen.presenter.home.profile.skillAdd

import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User.User

sealed class StateSkillsAdd{
    data class Success(val skills:List<Skill>?): StateSkillsAdd()
    object Loading: StateSkillsAdd()
    data class SuccessSave(val newUser: User): StateSkillsAdd()
    object Error: StateSkillsAdd()
}
