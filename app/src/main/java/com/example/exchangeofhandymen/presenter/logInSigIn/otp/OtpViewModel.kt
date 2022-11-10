package com.example.exchangeofhandymen.presenter.logInSigIn.otp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.entity.PhoneAuthResult
import com.example.exchangeofhandymen.domain.VerificateUseCase
import com.example.exchangeofhandymen.entity.CustomException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OtpViewModel  @Inject constructor(private val verificateUseCase: VerificateUseCase): ViewModel() {

    private val _state = MutableStateFlow<StateOtp>(StateOtp.Start)
    val state = _state.asStateFlow()



    fun resentCode(number: String,token: PhoneAuthProvider.ForceResendingToken){
        viewModelScope.launch {
            _state.value= StateOtp.Loading
            try {
               val rezult= verificateUseCase.resendVerification(number, token)
                if (rezult is PhoneAuthResult.CodeSent) {
                    if (rezult.token != null)
                        _state.value= StateOtp.SuccessResent(rezult.verificationId, rezult.token)
                    else
                        throw CustomException("Error verification")
                } else {
                    throw CustomException("Error verification")
                }
            }catch (e:Exception){
                _state.value= StateOtp.Error(e.message.toString())
            }
        }
    }

    fun checkCode(otp: String,typedOTP:String){
        viewModelScope.launch {
            _state.value= StateOtp.Loading
            try {
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                otp, typedOTP
            )
            if (typedOTP.isNotEmpty()) {
                if (typedOTP.length == 6) {
                    val rezult= verificateUseCase.checkCode(credential)
                    Log.d("otpReacult","$rezult")
                    if (rezult is PhoneAuthResult.VerificationCompleted) {
                        _state.value= StateOtp.SuccessCheck(rezult.newProfile)
                    } else {
                        throw CustomException("Error verification")
                    }
                } else {
                    throw CustomException("Please Enter Correct OTP")
                }
            } else {
                throw CustomException("Please Enter OTP")
            }
            }catch (e:Exception){
                _state.value= StateOtp.Error(e.message.toString())
            }
        }
    }




}