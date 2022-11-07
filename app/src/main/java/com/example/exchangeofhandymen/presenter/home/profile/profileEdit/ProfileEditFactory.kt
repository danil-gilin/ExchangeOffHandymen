package com.example.exchangeofhandymen.presenter.home.profile.profileEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ProfileEditFactory @Inject constructor(private val profileEditViewModel: ProfileEditViewModel) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return profileEditViewModel as T
    }
}