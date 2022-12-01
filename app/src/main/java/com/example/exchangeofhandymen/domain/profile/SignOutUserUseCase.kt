package com.example.exchangeofhandymen.domain.profile

import com.example.exchangeofhandymen.data.home.ProfileRepository
import com.example.exchangeofhandymen.data.home.WorkerRepository
import javax.inject.Inject

class SignOutUserUseCase @Inject constructor(private val repositoryWorker: WorkerRepository,private val repositoryProfile:ProfileRepository) {
    suspend fun sigOutUseCase(){
        repositoryWorker.deleteWorker()
        repositoryProfile.deleteInfo()
    }
}