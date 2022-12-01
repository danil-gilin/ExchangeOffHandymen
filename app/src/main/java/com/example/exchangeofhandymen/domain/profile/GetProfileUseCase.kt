package com.example.exchangeofhandymen.domain.profile

import com.example.exchangeofhandymen.data.home.ProfileRepository
import com.example.exchangeofhandymen.entity.User.User
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository){

   suspend fun getProfile(uid:String): User {
       return profileRepository.getProfile(uid)
   }
}