package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.login.LoginRepository
import com.example.exchangeofhandymen.entity.PhoneAuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class VerificateUseCase @Inject constructor (private val repository: LoginRepository) {
    suspend  fun verificate(number:String): PhoneAuthResult {
     return repository.verificate(number)
    }

    suspend fun resendVerification(number:String,token: PhoneAuthProvider.ForceResendingToken): PhoneAuthResult {
        return repository.resendVerificationCode(number,token)
    }

    suspend fun checkCode(credential: PhoneAuthCredential): PhoneAuthResult {
        return repository.checkCode(credential)
    }
}