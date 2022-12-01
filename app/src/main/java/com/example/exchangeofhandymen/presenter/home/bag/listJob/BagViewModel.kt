package com.example.exchangeofhandymen.presenter.home.bag.listJob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.job.DeleteJobUseCase
import com.example.exchangeofhandymen.domain.job.GetJobUseCase
import com.example.exchangeofhandymen.domain.job.WorkerDeleteJobUseCase
import com.example.exchangeofhandymen.domain.job.WorkerSelectJobUseCase
import com.example.exchangeofhandymen.domain.worker.GetProfileIsWorkerUseCase
import com.example.exchangeofhandymen.entity.job.JobWithId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BagViewModel @Inject constructor(
    private val getJobUseCase: GetJobUseCase,
    private val deleteJobUseCase: DeleteJobUseCase,
    private val workerDeleteJobUseCase: WorkerDeleteJobUseCase,
    private val workerSelectJobUseCase: WorkerSelectJobUseCase,
    private val getProfileIsWorker: GetProfileIsWorkerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<BagState>(BagState.Loading)
    val state = _state.asStateFlow()


   fun getJobForWork(uid: String) {
         viewModelScope.launch {
             try {
                 _state.value = BagState.Loading
                 val listjob = getJobUseCase.getJobWorker(uid)
                 if (listjob.isEmpty()){
                     _state.value= BagState.Error("Нет приглашений на работу")
                 }else{
                     _state.value = BagState.Success(listjob)
                 }
             } catch (e: Exception) {
                 _state.value= BagState.Error("Проблемы с интернетом")
             }
         }
     }

    fun getJobEmployer(uid: String) {
        viewModelScope.launch {
            try {
                _state.value = BagState.Loading
                val listjob = getJobUseCase.getJobEmployer(uid)
                if (listjob.isEmpty()){
                    _state.value= BagState.Error("Вы не создали работу")
                }else{
                    _state.value = BagState.Success(listjob)
                }
            } catch (e: Exception) {
                _state.value= BagState.Error("Проблемы с интернетом")
            }
        }
    }

    fun deleteJob(id: String, listJob: List<JobWithId>,imgList:List<String>) {
        viewModelScope.launch {
            try {
                _state.value = BagState.Loading
                deleteJobUseCase.deleteJob(id,imgList)
                val newList = listJob.filter{ it.id!=id }
                if (newList.isEmpty()){
                    _state.value= BagState.Error("Вы не создали работу")
                }else{
                    _state.value = BagState.Success(newList)
                }
            } catch (e: Exception) {
                _state.value= BagState.Error("Проблемы с интернетом")
            }
        }
    }

    fun deleteWorkerJob(idJob: String, idWorker: String,listJob: List<JobWithId> ) {
        viewModelScope.launch {
            try {
                _state.value = BagState.Loading
                workerDeleteJobUseCase.workerDeleteJobUseCase(idJob, idWorker)
                val newList = listJob.filter{ it.id!=idJob }
                if (newList.isEmpty()){
                    _state.value= BagState.Error("Нет приглашений на работу")
                }else{
                    _state.value = BagState.Success(newList)
                }
            } catch (e: Exception) {
                _state.value= BagState.Error("Проблемы с интернетом")
            }
        }
    }

    fun selectWorkerJob(idJob: String, idWorker: String,listJob: List<JobWithId>) {
        viewModelScope.launch {
            try {
                _state.value = BagState.Loading
                workerSelectJobUseCase.workerSelectJobUseCase(idJob,idWorker)
                val newList = listJob.filter{ it.id!=idJob }
                if (newList.isEmpty()){
                    _state.value= BagState.Error("Нет приглашений на работу")
                }else{
                    _state.value = BagState.Success(newList)
                }
            } catch (e: Exception) {
                _state.value= BagState.Error("Проблемы с интернетом")
            }
        }
    }

    fun isWorker(uidUser: String) {
        viewModelScope.launch {
            try {
                _state.value = BagState.Loading
               val isWoker = getProfileIsWorker.getProfileIsWork()
                _state.value = BagState.Start(isWoker)
            } catch (e: Exception) {
                _state.value= BagState.Error("Проблемы с интернетом")
            }
        }
    }

}