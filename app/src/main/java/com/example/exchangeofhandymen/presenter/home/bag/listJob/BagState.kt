package com.example.exchangeofhandymen.presenter.home.bag.listJob

import com.example.exchangeofhandymen.entity.job.JobWithId

sealed class BagState{
    data class Success(val jobs: List<JobWithId>): BagState()
    data class  Error(val error:String): BagState()
    object Loading: BagState()
    data class Start(val isWorker:Boolean): BagState()
}
