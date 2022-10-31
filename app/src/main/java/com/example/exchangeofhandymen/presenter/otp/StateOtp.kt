package com.example.exchangeofhandymen.presenter.otp

import com.example.exchangeofhandymen.presenter.login.StateLogIn
import com.google.firebase.auth.PhoneAuthProvider

sealed class StateOtp{
    object Start:StateOtp()
    data class  SuccessResent(val verificationId:String,val token: PhoneAuthProvider.ForceResendingToken): StateOtp()
    object SuccessCheck: StateOtp()
    object Loading:StateOtp()
    data class Error(val message:String):StateOtp()
}
