package com.example.exchangeofhandymen.presenter.home.workers.workersList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.worker.GetWorkerListUseCase
import com.example.exchangeofhandymen.entity.worker.WorrkerInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkersViewModel @Inject constructor(private val getWorkerListUseCase: GetWorkerListUseCase) : ViewModel() {
    private val _state= MutableStateFlow<WorkersState>(WorkersState.Start)
    val state=_state.asStateFlow()


    fun getListWorkerGeo(userUid: String) {
        viewModelScope.launch {
            _state.value = WorkersState.Loading
            try {
                val listWoker = getWorkerListUseCase.getGeoWorkerList(userUid)
                if (listWoker.isEmpty()){
                    _state.value = WorkersState.Error("Рядом с вами нет рабочих","Проверьте указали ли вы в профиле сове местоположение")
                }else{
                    _state.value = WorkersState.Success(listWoker)
                    val listNewFirebaseWorker=getWorkerListUseCase.getNewGeoWorker(userUid)
                    if(listWoker.map { it.id }.minus(listNewFirebaseWorker.map { it.id })== emptyList<WorrkerInt>() && listNewFirebaseWorker.map { it.id }.minus(listWoker.map { it.id })== emptyList<WorrkerInt>() ){

                    }else{
                        _state.value = WorkersState.Success(listNewFirebaseWorker)
                    }
                }
            } catch (e: Exception) {
                Log.d("DBWorker",e.toString())
                _state.value = WorkersState.Error("Проблемы с сетью","Проверьте подключение к интернету")
            }
        }
    }

}