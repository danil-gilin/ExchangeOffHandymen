package com.example.exchangeofhandymen.presenter.home.bag.listJob

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import com.example.exchangeofhandymen.databinding.CustomCounterBinding


class MyCustomCounter @JvmOverloads constructor
    (context: Context,atrributeSet:AttributeSet?=null, defStyleAttr: Int=0)
    :ConstraintLayout(context,atrributeSet,defStyleAttr) {
    private var maxCount=10
    private var minCount=0
    private var stepCount=1


    val binding= CustomCounterBinding.inflate(LayoutInflater.from(context))
        init {
            addView(binding.root)

            binding.editTextNumber.setText(minCount.toString())

            binding.btnPlus.setOnClickListener {
                var count= binding.editTextNumber.text.toString().toInt()
                if(count+stepCount<=maxCount){
                    count=count+stepCount
                }
                binding.editTextNumber.setText(count.toString())
            }

            binding.btnMinus.setOnClickListener {
                var count= binding.editTextNumber.text.toString().toInt()
                if(count-stepCount>=minCount){
                    count=count-stepCount
                }
                binding.editTextNumber.setText(count.toString())
            }

            binding.editTextNumber.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().isNotEmpty()){
                    if(s.toString().toInt()>maxCount){
                        binding.editTextNumber.setText(maxCount.toString())
                        binding.editTextNumber.setSelection( binding.editTextNumber.length())
                    }
                    if(s.toString().toInt()<0){
                        binding.editTextNumber.setText(minCount.toString())
                        binding.editTextNumber.setSelection( binding.editTextNumber.length())
                    }
                    }
                }

            })
        }

    fun changeMaxCount(max:Int){
        maxCount=max
    }

    fun changeMinCount(min:Int){
        minCount=min
        binding.editTextNumber.setText(minCount.toString())
    }

    fun changeStepCount(step:Int){
        stepCount=step
    }

    fun getCount():String{
        return binding.editTextNumber.text.toString()
    }


}