package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.ProfileRepository
import com.example.exchangeofhandymen.data.profile.SkillRepository
import com.example.exchangeofhandymen.entity.Skill
import javax.inject.Inject

class AddSkillUseCase @Inject constructor(private val skillRepository: SkillRepository) {

    suspend fun addNewSkill(skill:Skill):String{
        return skillRepository.addNewSkill(skill)
    }

    suspend fun addOldSkill(skillId:String){
        skillRepository.addOldSkill(skillId)
    }
}