package com.example.exchangeofhandymen.presenter.login

import com.google.firebase.auth.PhoneAuthProvider

sealed class StateLogIn{
    object Start: StateLogIn()
    data class  Success(val verificationId:String,val token: PhoneAuthProvider.ForceResendingToken):
        StateLogIn()
    object Loading: StateLogIn()
    data class Error(val message:String): StateLogIn()
}