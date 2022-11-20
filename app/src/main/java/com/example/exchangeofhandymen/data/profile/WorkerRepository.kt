package com.example.exchangeofhandymen.data.profile

import android.util.Log
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.Worker
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkerRepository @Inject constructor(private val skillRepository: SkillRepository) {
    private val dbFirestoreProfile: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    suspend fun getGeoWorkerList(uidUser: String): List<Worker> {
        val geoposition = dbFirestoreProfile.collection("User").document(uidUser).get().await()
            .getGeoPoint("geoPoint")
        var userGeo: GeoPosition? = null
        if (geoposition != null) {
            userGeo = GeoPosition(geoposition!!.latitude, geoposition!!.longitude)
            var workerlist = arrayListOf<Worker>()
            val userList =
                dbFirestoreProfile.collection("User").whereEqualTo("wokerFlag", true).get()
                    .await().documents

            userList.forEach {
                val id = it.id
                if (id != uidUser) {
                    val description = it.get("description").toString()
                    val geopositionWorker = it.getGeoPoint("geoPoint")
                    var mygeo: GeoPosition? = null
                    if (geopositionWorker != null) {
                        mygeo =
                            GeoPosition(geopositionWorker!!.latitude, geopositionWorker!!.longitude)
                    }

                    val mail = it.get("email").toString()
                    val name = it.get("name").toString()
                    val phone = it.get("phone").toString()
                    val rating = it.get("rating")?.toString()?.toDouble()
                    val skillsString = it.get("skills") as List<String>?
                    val wokerFlag = it.getBoolean("wokerFlag") == true
                    val img = it.getString("img").toString()


                    var skills: List<Skill>? = null
                    if (skillsString != null) {
                        skills = skillRepository.getSkills(skillsString).sortedBy { it.population }
                            .reversed().take(3)
                    }

                    val distance = mygeo?.let { it1 -> getCircleGeo(userGeo, it1, 60.0) }

                    if (distance?.second == true) {
                        val worker = Worker(
                            id,
                            name,
                            phone,
                            mail,
                            rating,
                            mygeo,
                            skills,
                            description,
                            wokerFlag,
                            img,
                            distance.first
                        )
                        workerlist.add(worker)
                    }
                }
            }

            return workerlist
        } else {
            val workerList =
                dbFirestoreProfile.collection("User").whereEqualTo("wokerFlag", true).get().await()
            return arrayListOf<Worker>()
        }
    }


    private fun getCircleGeo(
        userGeo: GeoPosition,
        workerGeo: GeoPosition,
        maxDistance: Double
    ): Pair<String, Boolean> {
        val x = workerGeo.latitude - userGeo.latitude
        val y = workerGeo.longitude - userGeo.longitude
        val distance = SphericalUtil.computeDistanceBetween(
            LatLng(workerGeo.latitude, workerGeo.longitude),
            LatLng(userGeo.latitude, userGeo.longitude)
        )
        Log.d("WorkerListForGeo", (distance / 1000).toString())
        return Pair((distance / 1000).toString(), (distance / 1000) < maxDistance)
    }

    suspend fun getWorkerList(uidUser: List<String>): List<Worker> {
        var listWoker = arrayListOf<Worker>()
        val geoposition =
            dbFirestoreProfile.collection("User").document(auth.currentUser?.uid.toString()).get()
                .await()
                .getGeoPoint("geoPoint")
        var userGeo: GeoPosition? = null
        if (geoposition != null) {
            userGeo = GeoPosition(geoposition!!.latitude, geoposition!!.longitude)
        }
        uidUser.forEach {
            val hashWorker = dbFirestoreProfile.collection("User").document(it).get().await()
            val description = hashWorker.get("description").toString()
            val geopositionWorker = hashWorker.getGeoPoint("geoPoint")
            var mygeo: GeoPosition? = null
            if (geopositionWorker != null) {
                mygeo =
                    GeoPosition(geopositionWorker!!.latitude, geopositionWorker!!.longitude)
            }

            val mail = hashWorker.get("email").toString()
            val name = hashWorker.get("name").toString()
            val phone = hashWorker.get("phone").toString()
            val rating = hashWorker.get("rating")?.toString()?.toDouble()
            val skillsString = hashWorker.get("skills") as List<String>?
            val wokerFlag = hashWorker.getBoolean("wokerFlag") == true
            val img = hashWorker.getString("img").toString()


            var skills: List<Skill>? = null
            if (skillsString != null) {
                skills = skillRepository.getSkills(skillsString).sortedBy { it.population }
                    .reversed().take(3)
            }

            var distance = (Pair<String, Boolean>("", false))
            if (userGeo != null) {
                distance = mygeo?.let { it1 -> getCircleGeo(userGeo, it1, 60.0) }!!
            }
            val worker = Worker(
                it,
                name,
                phone,
                mail,
                rating,
                mygeo,
                skills,
                description,
                wokerFlag,
                img,
                distance.first
            )
            listWoker.add(worker)
        }
        return listWoker
    }
}