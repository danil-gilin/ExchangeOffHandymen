package com.example.exchangeofhandymen.presenter.home.bag.jobInfoFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.worker.GetProfileIsWorkerUseCase
import com.example.exchangeofhandymen.domain.worker.GetWorkerListUseCase
import com.example.exchangeofhandymen.entity.worker.Worker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JobInfoViewModel @Inject constructor(private val getWorkerListUseCase: GetWorkerListUseCase, private val getProfileIsWorkerUseCase: GetProfileIsWorkerUseCase) : ViewModel() {

    private val _listWorker= Channel<List<Worker>>{  }
    val listWorker=_listWorker.receiveAsFlow()

    fun getListAcceptJob(workerAccept: List<String>) {
        viewModelScope.launch {
            if(getProfileIsWorkerUseCase.getProfileIsWork()){
                _listWorker.send(emptyList())
            }else{
                _listWorker.send(getWorkerListUseCase.getWorkerList(workerAccept))
            }
        }
    }

}