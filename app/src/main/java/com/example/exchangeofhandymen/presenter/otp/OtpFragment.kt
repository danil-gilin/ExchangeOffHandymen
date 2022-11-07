package com.example.exchangeofhandymen.presenter.otp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentOtpBinding
import com.example.exchangeofhandymen.presenter.home.homeNavigation.HomeNavFragment
import com.example.exchangeofhandymen.presenter.main.MainFragment
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : Fragment() {

    companion object {
        fun newInstance() = OtpFragment()
    }

    @Inject
    lateinit var factory: OtpViewModelFactory


    private val viewModel: OtpViewModel by viewModels { factory }
    private lateinit var binding: FragmentOtpBinding
    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater)
    arguments.let {
            OTP = it?.getString("OTP").toString()
            resendToken = it?.getParcelable("resendToken")!!
            phoneNumber = it.getString("phoneNumber")!!
        }


        binding.resendTextView.setOnClickListener {
            viewModel.resentCode(phoneNumber,resendToken)
            binding.resendTextView.visibility = View.INVISIBLE
            binding.resendTextView.isEnabled = false
        }

        binding.verifyOTPBtn.setOnClickListener {
            val typedOTP = (binding.otpEditText1.text.toString() + binding.otpEditText2.text.toString() + binding.otpEditText3.text.toString()
                        + binding.otpEditText4.text.toString() + binding.otpEditText5.text.toString() + binding.otpEditText6.text.toString())
            viewModel.checkCode(OTP,typedOTP)
        }

        binding.backToPhone.setOnClickListener {
            val navOptions: NavOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.to_right_in).setExitAnim(R.anim.to_right_out)
                .build()

            findNavController().navigate(R.id.action_otpFragment_to_logInFragment,null,navOptions=navOptions)


        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect{
                when(it){
                    is StateOtp.Error -> {
                        binding.otpProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(getActivity(),it.message,Toast.LENGTH_SHORT).show();
                    }
                    StateOtp.Loading ->{
                        binding.otpProgressBar.visibility = View.VISIBLE
                    }
                    StateOtp.Start -> {
                        Glide.with(binding.imgLotpPteview.context).load(R.drawable.otp_preview).centerInside().into(binding.imgLotpPteview)
                        binding.otpProgressBar.visibility = View.INVISIBLE
                        addTextChangeListener()
                        resendOTPTvVisibility()
                    }
                   is StateOtp.SuccessCheck -> {
                        val bundle=Bundle()
                        bundle.putString("phoneNumber",phoneNumber)
                        bundle.putBoolean("newUser",it.newProfile)

                       val newFragment=HomeNavFragment()
                       newFragment.arguments=bundle
                       activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_content_nav,newFragment)?.commit()

                        binding.otpProgressBar.visibility = View.INVISIBLE
                    }
                    is StateOtp.SuccessResent -> {
                        resendOTPTvVisibility()
                        binding.otpProgressBar.visibility = View.INVISIBLE
                        OTP = it.verificationId
                        resendToken = it.token
                    }
                }
            }
        }



        return binding.root
    }


    private fun resendOTPTvVisibility() {
        binding.otpEditText1.setText("")
        binding.otpEditText2.setText("")
        binding.otpEditText3.setText("")
        binding.otpEditText4.setText("")
        binding.otpEditText5.setText("")
        binding.otpEditText6.setText("")
        binding.resendTextView.visibility = View.INVISIBLE
        binding.resendTextView.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            binding.resendTextView.visibility = View.VISIBLE
            binding.resendTextView.isEnabled = true
        }, 30000)
    }

    private fun addTextChangeListener() {
        binding.otpEditText1.addTextChangedListener(EditTextWatcher(binding.otpEditText1))
        binding.otpEditText2.addTextChangedListener(EditTextWatcher(binding.otpEditText2))
        binding.otpEditText3.addTextChangedListener(EditTextWatcher(binding.otpEditText3))
        binding.otpEditText4.addTextChangedListener(EditTextWatcher(binding.otpEditText4))
        binding.otpEditText5.addTextChangedListener(EditTextWatcher(binding.otpEditText5))
        binding.otpEditText6.addTextChangedListener(EditTextWatcher(binding.otpEditText6))
    }


    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when (view.id) {
                binding.otpEditText1.id -> if (text.length == 1) binding.otpEditText2.requestFocus()
                binding.otpEditText2.id -> if (text.length == 1) binding.otpEditText3.requestFocus() else if (text.isEmpty()) binding.otpEditText1.requestFocus()
                binding.otpEditText3.id -> if (text.length == 1) binding.otpEditText4.requestFocus() else if (text.isEmpty()) binding.otpEditText2.requestFocus()
                binding.otpEditText4.id -> if (text.length == 1) binding.otpEditText5.requestFocus() else if (text.isEmpty()) binding.otpEditText3.requestFocus()
                binding.otpEditText5.id -> if (text.length == 1) binding.otpEditText6.requestFocus() else if (text.isEmpty()) binding.otpEditText4.requestFocus()
                binding.otpEditText6.id -> if (text.isEmpty()) binding.otpEditText5.requestFocus()
            }
        }

    }
}