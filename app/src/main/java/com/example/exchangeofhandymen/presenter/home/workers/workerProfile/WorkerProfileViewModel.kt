package com.example.exchangeofhandymen.presenter.home.workers.workerProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.GetProfileIsWorkerUseCase
import com.example.exchangeofhandymen.presenter.home.bag.listJob.BagState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkerProfileViewModel @Inject constructor(private val getProfileIsWorker: GetProfileIsWorkerUseCase): ViewModel() {
    private val _isWoker= Channel<Boolean>{}
    val isWorker=_isWoker.receiveAsFlow()



    fun getIsWorker() {
            viewModelScope.launch {
                try {
                    val isWoker = getProfileIsWorker.getProfileIsWork()
                    Log.d("isWorker",isWoker.toString())
                    _isWoker.send(isWoker)
                } catch (e: Exception) {

                }
        }

    }

}