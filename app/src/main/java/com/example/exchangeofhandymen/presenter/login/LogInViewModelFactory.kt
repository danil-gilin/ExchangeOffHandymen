package com.example.exchangeofhandymen.presenter.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class LogInViewModelFactory @Inject constructor(private val logInViewModel: LogInViewModel):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return logInViewModel as T
    }
}