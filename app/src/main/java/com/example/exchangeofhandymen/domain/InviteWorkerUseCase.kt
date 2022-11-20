package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.JobRepository
import javax.inject.Inject

class InviteWorkerUseCase @Inject constructor(private val jobRepository: JobRepository) {

   suspend fun inviteWorker(id: String, workerUid: String){
        jobRepository.inviteWorker(id,workerUid)
    }
}