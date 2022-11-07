package com.example.exchangeofhandymen.presenter.home.profile.profileEdit

import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.StateProfile

sealed class ProfEditState{
    data class Success(val skills:List<Skill>?): ProfEditState()
    object SuccessSave: ProfEditState()
    object Loading: ProfEditState()
    object Start: ProfEditState()
    data class  Error(val errorName:String?,val errorEmail:String?,val errorDescription:String?): ProfEditState()
}
