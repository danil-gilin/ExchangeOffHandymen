package com.example.exchangeofhandymen.domain

import com.example.exchangeofhandymen.data.profile.PhotoRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    fun deletePhoto(){
        photoRepository.deletePhoto()
    }
}