package com.example.exchangeofhandymen.entity

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class PhoneAuthResult {
    data class VerificationCompleted(val credentials: PhoneAuthCredential,val newProfile:Boolean=false) : PhoneAuthResult()
    data class CodeSent(val verificationId: String, val token: PhoneAuthProvider.ForceResendingToken?)
        : PhoneAuthResult()
    data class ErrorCode(val message:String="Error verification"): PhoneAuthResult()
}
