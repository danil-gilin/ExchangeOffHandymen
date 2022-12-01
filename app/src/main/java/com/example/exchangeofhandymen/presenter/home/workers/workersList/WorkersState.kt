package com.example.exchangeofhandymen.presenter.home.workers.workersList

import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.entity.worker.WorrkerInt

sealed class WorkersState {
    data class Success(val worker: List<WorrkerInt>): WorkersState()
    object Loading: WorkersState()
    object Start: WorkersState()
    data class Error(val preview:String,val message:String): WorkersState()
}