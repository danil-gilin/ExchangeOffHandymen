package com.example.exchangeofhandymen.presenter.login

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.exchangeofhandymen.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.databinding.FragmentLogInBinding
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment() {

    companion object {
        fun newInstance() = LogInFragment()
    }

    @Inject
    lateinit var logInFactory: LogInViewModelFactory


    private val auth = FirebaseAuth.getInstance()
    private val viewModel: LogInViewModel by viewModels { logInFactory }
    private lateinit var binding: FragmentLogInBinding
    private lateinit var number: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater)
        binding.phoneEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher("RU"))


        binding.btnLogIn.setOnClickListener {
          try {
                number = binding.phoneEdit.text?.trim().toString()
                if(number[0]=='8'){
                    number=number.drop(1)
                    Log.d("number_auth", "number"+number)
                    number="+7"+number
                    Log.d("number_auth", "number"+number)
                }
                viewModel.verificate(number)
            }catch (e:Exception){
                //ведите корректный номер
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect{
                when(it){
                   is StateLogIn.Error -> {
                       Log.d("number_auth", it.message)
                    }
                    StateLogIn.Loading -> {
                        Log.d("number_auth", "onVerificationCompleted 2")
                    }
                    StateLogIn.Start -> {
                        Glide.with(binding.imgLogInPteview.context).load(R.drawable.log_in_preview).centerInside().into(binding.imgLogInPteview)
                        Log.d("number_auth", "onVerificationCompleted 3")
                    }
                   is StateLogIn.Success -> {
                       Log.d("number_auth", "onVerificationCompleted 4")
                        val bundle=Bundle()
                        bundle.putString("OTP" ,it.verificationId )
                        bundle.putParcelable("resendToken" , it.token)
                        bundle.putString("phoneNumber" , number)


                        findNavController().navigate(R.id.action_logInFragment_to_otpFragment,bundle)
                    }
                }
            }
        }


        return binding.root
    }
}