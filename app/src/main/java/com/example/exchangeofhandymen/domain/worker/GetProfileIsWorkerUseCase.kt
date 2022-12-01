package com.example.exchangeofhandymen.domain.worker

import com.example.exchangeofhandymen.data.home.ProfileRepository
import javax.inject.Inject

class GetProfileIsWorkerUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend fun getProfileIsWork():Boolean {
      return  profileRepository.getProfileIsWorker()
    }
}