package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.ProfileRepository
import com.example.exchangeofhandymen.data.profile.SkillRepository
import com.example.exchangeofhandymen.entity.Skill
import javax.inject.Inject

class GettSkillsUseCase @Inject constructor(private val skillRepository: SkillRepository) {
    suspend fun getSkills(skillsId:List<String>):List<Skill>{
     return   skillRepository.getSkills(skillsId)
    }
}