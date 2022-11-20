package com.example.exchangeofhandymen.presenter.home.workers.workerProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class WorkerProfileFactory @Inject constructor(private val workerProfileViewModel: WorkerProfileViewModel):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return workerProfileViewModel as T
    }
}