package com.example.exchangeofhandymen.data.profile

import android.graphics.drawable.Drawable
import android.net.Uri
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class PhotoRepository @Inject constructor() {
  private  val storage = Firebase.storage.reference
    private val auth=FirebaseAuth.getInstance()
    private val dbFirestoreProfile: FirebaseFirestore = FirebaseFirestore.getInstance()


    suspend fun savePhoto(uri: Uri):String{
       val refStorage= storage.child("avatarUser/${auth.currentUser?.uid}")
        refStorage.putFile(uri).await()
        val uri=refStorage.downloadUrl.await().toString()

        return uri
    }

    fun deletePhoto() {
        dbFirestoreProfile.collection("User").document(auth.currentUser!!.uid).update("img","")

        val refStorage= storage.child("avatarUser/${auth.currentUser?.uid}")
        refStorage.delete()
    }


}