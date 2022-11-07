package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.ProfileRepository
import com.example.exchangeofhandymen.data.profile.SkillRepository
import com.example.exchangeofhandymen.entity.Skill
import javax.inject.Inject

class GetAllSkillsUseCase @Inject  constructor(private val skillRepository: SkillRepository) {
    suspend fun getAllSkills(): ArrayList<Skill> {
        return skillRepository.getAllSkills()
    }
}