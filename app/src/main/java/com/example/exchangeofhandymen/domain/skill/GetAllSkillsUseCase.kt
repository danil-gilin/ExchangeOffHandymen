package com.example.exchangeofhandymen.domain.skill

import com.example.exchangeofhandymen.data.home.SkillRepository
import com.example.exchangeofhandymen.entity.Skill
import javax.inject.Inject

class GetAllSkillsUseCase @Inject  constructor(private val skillRepository: SkillRepository) {
    suspend fun getAllSkills(): ArrayList<Skill> {
        return skillRepository.getAllSkills()
    }
}