package com.example.exchangeofhandymen.domain.skill

import com.example.exchangeofhandymen.data.home.SkillRepository
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