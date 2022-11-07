package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.SkillRepository
import com.example.exchangeofhandymen.entity.Skill
import javax.inject.Inject

class DeleteSkillUseCase @Inject constructor (private val skillRepository: SkillRepository) {

    suspend fun deleteSkill(skillId: ArrayList<String>) : List<Skill>? {
            return skillRepository.deleteSkill(skillId)
    }
}