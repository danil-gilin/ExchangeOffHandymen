package com.example.exchangeofhandymen.presenter.home.workers.workersList

import com.example.exchangeofhandymen.entity.Worker

sealed class WorkersState {
    data class Success(val worker: List<Worker>): WorkersState()
    object Loading: WorkersState()
    object Start: WorkersState()
    data class Error(val preview:String,val message:String): WorkersState()
}