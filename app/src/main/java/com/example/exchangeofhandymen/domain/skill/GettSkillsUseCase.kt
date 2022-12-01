package com.example.exchangeofhandymen.domain.skill

import com.example.exchangeofhandymen.data.home.SkillRepository
import com.example.exchangeofhandymen.entity.Skill
import javax.inject.Inject

class GettSkillsUseCase @Inject constructor(private val skillRepository: SkillRepository) {
    suspend fun getSkills(skillsId:List<String>):List<Skill>{
     return   skillRepository.getSkills(skillsId)
    }
}