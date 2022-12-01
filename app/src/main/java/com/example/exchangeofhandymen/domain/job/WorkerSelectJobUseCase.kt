package com.example.exchangeofhandymen.domain.job

import com.example.exchangeofhandymen.data.home.JobRepository
import javax.inject.Inject

class WorkerSelectJobUseCase @Inject constructor(private val jobRepository: JobRepository) {

    suspend fun workerSelectJobUseCase(uidJob:String,uidWorker:String){
        jobRepository.workerSelectJob(uidJob, uidWorker)
    }
}