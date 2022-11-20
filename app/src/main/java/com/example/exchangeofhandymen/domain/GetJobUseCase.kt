package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.JobRepository
import com.example.exchangeofhandymen.entity.job.JobWithId
import javax.inject.Inject

class GetJobUseCase @Inject constructor(private val jobRepository: JobRepository) {

    suspend fun getJobEmployer(uid:String,workerUid: String?=null):List<JobWithId>{
       return jobRepository.getJobEmployer(uid,workerUid)
    }

    suspend fun getJobWorker(uid:String):List<JobWithId>{
        return jobRepository.jobForWorker(uid)
    }

}