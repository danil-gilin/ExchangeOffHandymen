package com.example.exchangeofhandymen.presenter.home.bag.newJob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class NewJobFactory @Inject constructor(private val newJobViewModel: NewJobViewModel) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return newJobViewModel as T
    }
}