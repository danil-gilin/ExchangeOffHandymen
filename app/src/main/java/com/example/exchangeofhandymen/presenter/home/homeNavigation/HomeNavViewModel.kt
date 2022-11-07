package com.example.exchangeofhandymen.presenter.home.homeNavigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.data.profile.ProfileRepository
import com.example.exchangeofhandymen.domain.NewProfileUseCase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeNavViewModel @Inject constructor(private val newProfileUseCase: NewProfileUseCase) : ViewModel() {


    fun checkUser(currentUser: FirebaseUser?,number:String) {
        viewModelScope.launch {
            if (currentUser != null) {
                newProfileUseCase.newProfile(currentUser.uid, number)
            }
        }
    }


}