package com.example.exchangeofhandymen.domain.photo

import com.example.exchangeofhandymen.data.home.PhotoRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    fun deletePhoto(){
        photoRepository.deletePhoto()
    }
}