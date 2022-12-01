package com.example.exchangeofhandymen.domain.job

import com.example.exchangeofhandymen.data.home.JobRepository
import javax.inject.Inject

class WorkerDeleteJobUseCase @Inject constructor(private val jobRepository: JobRepository) {
    suspend fun workerDeleteJobUseCase(uidJob:String,uidWorker:String){
        jobRepository.workerDeleteJob(uidJob, uidWorker)
    }
}