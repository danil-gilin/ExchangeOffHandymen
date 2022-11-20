package com.example.exchangeofhandymen.presenter.home.workers.jobSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.GetJobUseCase
import com.example.exchangeofhandymen.domain.InviteWorkerUseCase
import com.example.exchangeofhandymen.presenter.home.bag.listJob.BagState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class JobSelecetViewModel @Inject constructor(
    private val getJobUseCase: GetJobUseCase,
    private val inviteWorkerUseCase: InviteWorkerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<JobSelectState>(JobSelectState.Loading)
    val state = _state.asStateFlow()


    fun getJobEmployer(uid: String,workerUid: String) {
        viewModelScope.launch {
            try {
                _state.value = JobSelectState.Loading
                val listjob = getJobUseCase.getJobEmployer(uid,workerUid)
                if (listjob.isEmpty()){
                    _state.value = JobSelectState.Error("У вас нет доступных работ")
                }else{
                    _state.value = JobSelectState.Success(listjob)
                }
            } catch (e: Exception) {
                _state.value = JobSelectState.Error("Ошибка подключения")
            }
        }
    }

    fun invite(id: String, workerUid: String) {
        viewModelScope.launch {
            try {
                _state.value = JobSelectState.Loading
                inviteWorkerUseCase.inviteWorker(id,workerUid)
                _state.value = JobSelectState.SuccessSelect
            } catch (e: Exception) {
                _state.value = JobSelectState.Error("Ошибка подключения")
            }
        }
    }
}