package com.example.exchangeofhandymen.presenter.home.bag.listJob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class BagFactory @Inject  constructor(private val bagViewModel: BagViewModel)  :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return bagViewModel as T
    }
}