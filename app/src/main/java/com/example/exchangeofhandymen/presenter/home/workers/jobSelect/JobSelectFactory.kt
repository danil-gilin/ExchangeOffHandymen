package com.example.exchangeofhandymen.presenter.home.workers.jobSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class JobSelectFactory  @Inject constructor(private val jobSelecetViewModel: JobSelecetViewModel) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return jobSelecetViewModel as T
    }
}