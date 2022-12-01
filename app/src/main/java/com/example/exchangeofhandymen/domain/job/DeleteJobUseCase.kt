package com.example.exchangeofhandymen.domain.job

import com.example.exchangeofhandymen.data.home.JobRepository
import javax.inject.Inject

class DeleteJobUseCase @Inject constructor(private val jobRepository: JobRepository) {
    suspend fun deleteJob(uidJob:String,imgList:List<String>){
        jobRepository.deleteJob(uidJob,imgList)
    }
}