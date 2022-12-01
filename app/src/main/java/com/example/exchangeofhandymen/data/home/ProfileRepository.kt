package com.example.exchangeofhandymen.data.home

import android.util.Log
import com.example.exchangeofhandymen.data.db.UserDao
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.User.User
import com.example.exchangeofhandymen.entity.User.UserDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val userDao: UserDao) {
    private val dbFirestoreProfile: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()



    suspend fun getProfile(uid: String): User {
        return when{
            getProfileDB(uid)!=null->getProfileDB(uid)!!
            else->getProfileFirebase(uid)
        }
    }

    suspend fun getProfileDB(uid: String): User? {
       val userDB= userDao.getUser(uid) ?: return null
        return User(userDB.name,userDB.phone,userDB.email,userDB.rating,userDB.geoPoint,userDB.skills,userDB.description,userDB.wokerFlag,userDB.img)
    }


    suspend fun getProfileFirebase(uid: String): User {
        var user: User? = null
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
        userDao.deleteUser()
        val userDB=UserDB(uid,user.name,user.phone,user.email,user.rating,user.geoPoint,user.skills,user.description,user.wokerFlag,user.img)
        userDao.addUser(userDB)
        return user!!
    }

    suspend fun newProfile(uid: String, number: String, worker: Boolean) {
        val newUser = User("Работник", number, "", 4.0, null, emptyList<String>(), "", worker,"")
        dbFirestoreProfile.collection("User").document(uid).set(newUser).await()
        val userDB=UserDB(uid,newUser.name,newUser.phone,newUser.email,newUser.rating,newUser.geoPoint,newUser.skills,newUser.description,newUser.wokerFlag,newUser.img)
        userDao.addUser(userDB)
    }


    suspend fun editProfile(user: User) {
        dbFirestoreProfile.collection("User").document(auth.currentUser?.uid.toString()).set(user)
            .await()
        userDao.deleteUser()
        val userSkills = dbFirestoreProfile.collection("User").document(auth.currentUser?.uid.toString()).get().await().get("skills") as List<String>?
        Log.d("DBUser",userSkills.toString())
        val userDB=UserDB(auth.currentUser?.uid.toString(),user.name,user.phone,user.email,user.rating,user.geoPoint,userSkills,user.description,user.wokerFlag,user.img)
        userDao.addUser(userDB)
    }

    suspend fun getProfileIsWorker():Boolean{
      return dbFirestoreProfile.collection("User").document(auth.currentUser?.uid.toString()).get().await().get("wokerFlag") as Boolean
    }

    suspend fun deleteInfo() {
        userDao.deleteUser()
    }
}