package com.example.exchangeofhandymen.domain.photo

import android.net.Uri
import com.example.exchangeofhandymen.data.home.PhotoRepository
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    suspend fun savePhoto( uri:Uri):String{
      return photoRepository.savePhoto(uri)
    }
}