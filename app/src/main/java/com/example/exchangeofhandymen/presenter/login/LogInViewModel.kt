package com.example.exchangeofhandymen.presenter.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.entity.PhoneAuthResult
import com.example.exchangeofhandymen.domain.VerificateUseCase
import com.example.exchangeofhandymen.entity.CustomException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogInViewModel @Inject constructor(private val verificateUseCase: VerificateUseCase) :
    ViewModel() {



    private val _state = MutableStateFlow<StateLogIn>(StateLogIn.Start)
    val state = _state.asStateFlow()

    init {
        _state.value=StateLogIn.Start
    }



    fun verificate(number: String) {
        viewModelScope.launch {
            _state.value = StateLogIn.Loading
            val rezult: PhoneAuthResult
            try {
                if (number.isNotEmpty()) {
                    if (number.length == 16) {
                        var numberTemp = number.replace(" ", "")
                        numberTemp = numberTemp.replace("-", "")
                        Log.d("number_auth", "onVerificationCompleted:$number")
                        rezult = verificateUseCase.verificate(numberTemp)
                    } else {
                        throw CustomException("Please Enter correct Number")
                    }
                } else {
                    throw CustomException("Please Enter correct Number")
                }
                if (rezult is PhoneAuthResult.CodeSent) {
                    if (rezult.token != null)
                        _state.value = StateLogIn.Success(rezult.verificationId, rezult.token)
                    else
                        throw CustomException("Error verification")
                } else {
                    throw CustomException("Error verification")
                }
                Log.d("number_auth", "onVerificationCompleted 1:$number")
            } catch (e: Exception) {
                Log.d("number_auth", "onVerificationCompleted 2:$e")
                _state.value = StateLogIn.Error(e.message.toString())
            }
        }
    }
}