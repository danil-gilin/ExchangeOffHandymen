package com.example.exchangeofhandymen.presenter.home.profile.profileEdit

import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.StateProfile

sealed class ProfEditState{
    data class Success(val skills:List<Skill>?): ProfEditState()
    object SuccessSave: ProfEditState()
    data class SuccessPhoto(val PhotoUrl:String): ProfEditState()
    object Loading: ProfEditState()
    object Start: ProfEditState()
    data class  Error(val errorName:String?,val errorEmail:String?,val errorDescription:String?): ProfEditState()
    data class  ErrorCustom(val customError:String): ProfEditState()
    data class  SuccessSaveGeo(val lat:Double,val lon:Double) : ProfEditState()
}
