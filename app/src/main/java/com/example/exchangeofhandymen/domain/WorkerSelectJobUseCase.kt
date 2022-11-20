package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.JobRepository
import javax.inject.Inject

class WorkerSelectJobUseCase @Inject constructor(private val jobRepository: JobRepository) {

    suspend fun workerSelectJobUseCase(uidJob:String,uidWorker:String){
        jobRepository.workerSelectJob(uidJob, uidWorker)
    }
}