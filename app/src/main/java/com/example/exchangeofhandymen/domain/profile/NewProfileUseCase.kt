package com.example.exchangeofhandymen.domain.profile

import com.example.exchangeofhandymen.data.home.ProfileRepository
import javax.inject.Inject

class NewProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun newProfile(number: String, uid: String, worker: Boolean) {
        profileRepository.newProfile(number, uid, worker)
    }
}