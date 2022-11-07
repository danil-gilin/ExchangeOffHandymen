package com.example.exchangeofhandymen.presenter.home.profile.skillAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class SkillsAddViewModelFactory @Inject constructor(private val skillsAddViewModel: SkillsAddViewModel) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return skillsAddViewModel as T
    }
}