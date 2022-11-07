package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.ProfileRepository
import javax.inject.Inject

class NewProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
   suspend fun newProfile(number:String,uid:String) {
        profileRepository.newProfile(number, uid)
    }
}