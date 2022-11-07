package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.ProfileRepository
import com.example.exchangeofhandymen.entity.User
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val profileRepository: ProfileRepository){

    suspend fun editProfile(user: User){
        profileRepository.editProfile(user)
    }
}