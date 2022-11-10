package com.example.exchangeofhandymen.domain

import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.exchangeofhandymen.data.profile.PhotoRepository
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    suspend fun savePhoto( uri:Uri):String{
      return photoRepository.savePhoto(uri)
    }
}