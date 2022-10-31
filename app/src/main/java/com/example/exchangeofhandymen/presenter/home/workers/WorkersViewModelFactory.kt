package com.example.exchangeofhandymen.presenter.home.workers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class WorkersViewModelFactory @Inject constructor(private val workersViewModel: WorkersViewModel):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return workersViewModel as T
    }
}