package com.example.exchangeofhandymen.presenter.home.bag.jobInfoFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class JobInfoFactory @Inject constructor(private val jobInfoViewModel: JobInfoViewModel) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return jobInfoViewModel as T
    }
}