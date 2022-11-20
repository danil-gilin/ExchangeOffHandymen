package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.ProfileRepository
import javax.inject.Inject

class GetProfileIsWorkerUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend fun getProfileIsWork():Boolean {
      return  profileRepository.getProfileIsWorker()
    }
}