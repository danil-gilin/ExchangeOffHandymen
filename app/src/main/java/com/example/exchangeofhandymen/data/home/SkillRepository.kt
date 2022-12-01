package com.example.exchangeofhandymen.data.home

import com.example.exchangeofhandymen.entity.Skill
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SkillRepository @Inject constructor() {
    private val dbFirestoreProfile: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getAllSkills(): ArrayList<Skill> {
        var listSkills = arrayListOf<Skill>()
        val allSkills = dbFirestoreProfile.collection("Skill").get().await()
        allSkills.documents.forEach {
            listSkills.add(
                Skill(
                    it.id,
                    it.get("Name") as String,
                    it.get("Population").toString().toInt()
                )
            )
        }
        return listSkills
    }


    suspend fun getSkills(skillsId: List<String>): List<Skill> {
        var skillsList = arrayListOf<Skill>()
        skillsId.forEach {
            val skill = dbFirestoreProfile.collection("Skill").document(it).get().await()
            val id = it
            val name = skill.get("Name").toString()
            val population = skill.get("Population").toString().toInt()
            skillsList.add(Skill(id, name, population))
        }
        return skillsList
    }

    suspend fun addNewSkill(skill: Skill): String {
        val skillHash = hashMapOf(
            "Name" to skill.name,
            "Population" to skill.population.toLong()
        )
        val idSkill = dbFirestoreProfile.collection("Skill").add(skillHash).await().id

        val newSkills = getArraySkill(auth.uid!!)?.plus(idSkill)
        dbFirestoreProfile.collection("User").document(auth.uid!!).update("skills", newSkills)
            .await()
        return idSkill
    }

    private suspend fun getArraySkill(userUid: String): List<String>? {
        val userHashMap = dbFirestoreProfile.collection("User").document(auth.uid!!).get().await()
        return userHashMap.get("skills") as List<String>?
    }

    suspend fun addOldSkill(skillId: String) {
        val newSkills = getArraySkill(auth.uid!!)?.plus(skillId)
        val oldPopulation =
            dbFirestoreProfile.collection("Skill").document(skillId).get().await().get("Population")
                .toString().toInt()

        dbFirestoreProfile.collection("Skill").document(skillId)
            .update("Population", oldPopulation + 1).await()

        dbFirestoreProfile.collection("User").document(auth.uid!!).update("skills", newSkills)
            .await()
    }

    suspend fun deleteSkill(skillId: ArrayList<String>): List<Skill>? {
        val newSkills = getArraySkill(auth.uid!!)?.minus(skillId.toSet())
        dbFirestoreProfile.collection("User").document(auth.uid!!).update("skills", newSkills)
            .await()
        skillId.forEach { skillId ->
            val oldPopulation =
                dbFirestoreProfile.collection("Skill").document(skillId).get().await()
                    .get("Population").toString().toInt()
            dbFirestoreProfile.collection("Skill").document(skillId)
                .update("Population", oldPopulation - 1).await()
        }

        val listSkillRezult = newSkills?.let { getSkills(it) }
        return listSkillRezult
    }
}