package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.ProfileRepository
import com.example.exchangeofhandymen.entity.User
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository){

   suspend fun getProfile(uid:String): User {
       return profileRepository.getProfile(uid)
   }
}