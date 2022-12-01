package com.example.exchangeofhandymen.presenter.home.profile.profileUser

import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User.User

sealed class StateProfile{
    data class Success(val user: User, val skills:List<Skill>?): StateProfile()
    object Loading: StateProfile()
    object Start: StateProfile()
    object Error: StateProfile()
}
