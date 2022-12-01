package com.example.exchangeofhandymen.domain.worker

import com.example.exchangeofhandymen.data.home.JobRepository
import javax.inject.Inject

class InviteWorkerUseCase @Inject constructor(private val jobRepository: JobRepository) {

   suspend fun inviteWorker(id: String, workerUid: String){
        jobRepository.inviteWorker(id,workerUid)
    }
}