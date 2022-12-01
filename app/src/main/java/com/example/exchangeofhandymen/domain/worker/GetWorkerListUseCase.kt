package com.example.exchangeofhandymen.domain.worker

import com.example.exchangeofhandymen.data.home.WorkerRepository
import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.entity.worker.WorrkerInt
import javax.inject.Inject

class GetWorkerListUseCase @Inject constructor(private val geoRepository: WorkerRepository) {

    suspend fun getGeoWorkerList(uidUser: String):List<WorrkerInt>{
     return geoRepository.getGeoWorkerList(uidUser)
    }

    suspend fun getNewGeoWorker(uidUser: String):List<WorrkerInt>{
        return geoRepository.getGeoWorkerListFirebase(uidUser)
    }

    suspend fun getWorkerList(uidUser: List<String>):List<Worker>{
        return geoRepository.getWorkerList(uidUser)
    }
}