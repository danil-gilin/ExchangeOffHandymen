package com.example.exchangeofhandymen.presenter.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class OtpViewModelFactory  @Inject constructor(private val otpViewModel: OtpViewModel):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return otpViewModel as T
    }
}