package com.example.exchangeofhandymen.presenter.home.workers.jobSelect

import com.example.exchangeofhandymen.entity.job.JobWithId

sealed class JobSelectState {
    data class Success(val jobs: List<JobWithId>): JobSelectState()
    object SuccessSelect: JobSelectState()
    data class Error(val message: String): JobSelectState()
    object Loading: JobSelectState()
}