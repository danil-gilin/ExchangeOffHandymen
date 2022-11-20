package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.WorkerRepository
import com.example.exchangeofhandymen.entity.Worker
import javax.inject.Inject

class GetWorkerListUseCase @Inject constructor(private val geoRepository: WorkerRepository) {

    suspend fun getGeoWorkerList(uidUser: String):List<Worker>{
     return geoRepository.getGeoWorkerList(uidUser)
    }

    suspend fun getWorkerList(uidUser: List<String>):List<Worker>{
        return geoRepository.getWorkerList(uidUser)
    }
}