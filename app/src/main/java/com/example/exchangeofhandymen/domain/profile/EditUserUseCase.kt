package com.example.exchangeofhandymen.domain.profile

import com.example.exchangeofhandymen.data.home.ProfileRepository
import com.example.exchangeofhandymen.entity.User.User
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val profileRepository: ProfileRepository){

    suspend fun editProfile(user: User){
        profileRepository.editProfile(user)
    }
}