package com.example.exchangeofhandymen.data.profile

import android.util.Log
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor() {
    private val dbFirestoreProfile: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getProfile(uid: String): User {
        var user: User? = null
        Log.d("GetUser", uid)
        val userHash = dbFirestoreProfile.collection("User").document(uid).get().await()
        val description = userHash.get("description").toString()
        val geoposition = userHash.getGeoPoint("geoPoint")
        var mygeo:GeoPosition?=null
        if(geoposition!=null){
            mygeo=GeoPosition(geoposition!!.latitude,geoposition!!.longitude)
        }
        val mail = userHash.get("email").toString()
        val name = userHash.get("name").toString()
        val phone = userHash.get("phone").toString()
        val rating = userHash.get("rating")?.toString()?.toDouble()
        val skills = userHash.get("skills") as List<String>?
        val wokerFlag = userHash.getBoolean("wokerFlag") == true
        val img = userHash.getString("img").toString()
        user = User(name, phone, mail, rating, mygeo, skills, description, wokerFlag,img)
        Log.d("GetUser", user.toString())
        return user!!
    }

    suspend fun newProfile(uid: String, number: String, worker: Boolean) {
        val newUser = User("Работник", number, "", 4.0, null, emptyList<String>(), "", worker,"")
        dbFirestoreProfile.collection("User").document(uid).set(newUser).await()
    }


    suspend fun editProfile(user: User) {
        dbFirestoreProfile.collection("User").document(auth.currentUser?.uid.toString()).set(user)
            .await()
    }


}