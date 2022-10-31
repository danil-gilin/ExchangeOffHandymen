package com.example.exchangeofhandymen.presenter.home.homeNavigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class HomeNavViewModelFactory @Inject constructor(private val homeNavViewModel: HomeNavViewModel):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return homeNavViewModel as T
    }
}