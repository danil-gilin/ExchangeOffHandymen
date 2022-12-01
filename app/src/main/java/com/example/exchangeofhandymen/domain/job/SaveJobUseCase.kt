package com.example.exchangeofhandymen.domain.job

import com.example.exchangeofhandymen.data.home.JobRepository
import com.example.exchangeofhandymen.entity.job.Job
import javax.inject.Inject

class SaveJobUseCase @Inject constructor(private val jobRepository: JobRepository){

    suspend fun saveJob(job: Job){
        jobRepository.saveJob(job)
    }
}