package com.example.exchangeofhandymen.data.login

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.exchangeofhandymen.entity.CustomException
import com.example.exchangeofhandymen.entity.PhoneAuthResult
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*

import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ActivityScoped
class LoginRepository @Inject constructor(@ActivityContext private val activity: Context){
    private val auth = FirebaseAuth.getInstance()

   suspend fun verificate(number: String): PhoneAuthResult =
        suspendCoroutine{cont->
                val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        cont.resume(PhoneAuthResult.VerificationCompleted(credential))
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        cont.resume(PhoneAuthResult.CodeSent("",null))
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        cont.resume(PhoneAuthResult.CodeSent(verificationId,token))
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(number)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity as Activity)
                    .setCallbacks(callback)
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
        }

   suspend fun  resendVerificationCode(number: String,token: PhoneAuthProvider.ForceResendingToken): PhoneAuthResult =
        suspendCoroutine{cont->
            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    cont.resume(PhoneAuthResult.VerificationCompleted(credential))
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    cont.resume(PhoneAuthResult.CodeSent("",null))
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    cont.resume(PhoneAuthResult.CodeSent(verificationId,token))
                }
            }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity as Activity)
            .setCallbacks(callback)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun checkCode(credential: PhoneAuthCredential): PhoneAuthResult =
        suspendCoroutine{cont->
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity as Activity) { task ->
                    if (task.isSuccessful) {
                        if (auth.currentUser != null) {
                            if (auth.currentUser?.metadata?.creationTimestamp == auth.currentUser?.metadata?.lastSignInTimestamp) {
                                cont.resume(PhoneAuthResult.VerificationCompleted(credential,true))
                            }else{
                                cont.resume(PhoneAuthResult.VerificationCompleted(credential))
                            }
                        }
                    } else {
                        cont.resume(PhoneAuthResult.ErrorCode())
                        Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {

                        }
                    }
                }
        }
}