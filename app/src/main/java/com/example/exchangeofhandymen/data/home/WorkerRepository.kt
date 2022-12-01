package com.example.exchangeofhandymen.data.home

import android.util.Log
import com.example.exchangeofhandymen.data.db.WorkerDao
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.entity.worker.WorkerDB
import com.example.exchangeofhandymen.entity.worker.WorrkerInt
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class WorkerRepository @Inject constructor(private val skillRepository: SkillRepository,private val workerDao: WorkerDao) {
    private val dbFirestoreProfile: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var localListWorker= emptyList<WorrkerInt>()

    suspend fun getGeoWorkerList(uidUser: String): List<WorrkerInt>{
        return when{
            getGeoWorkerListSQL() != emptyList<WorrkerInt>()-> {
                localListWorker= getGeoWorkerListSQL()
                localListWorker.reversed()
            }
            getGeoWorkerListFirebase(uidUser) != emptyList<WorrkerInt>()->getGeoWorkerListFirebase(uidUser)
            else-> emptyList()
        }
    }


    suspend fun getGeoWorkerListSQL(): List<WorrkerInt>{
      return  workerDao.getListWorker()
    }

    suspend fun getGeoWorkerListFirebase(uidUser: String): List<WorrkerInt>{

        val geoposition = dbFirestoreProfile.collection("User").document(uidUser).get().await()
            .getGeoPoint("geoPoint")
        var userGeo: GeoPosition? = null
        if (geoposition != null) {
            userGeo = GeoPosition(geoposition!!.latitude, geoposition!!.longitude)
            var workerlist = arrayListOf<Worker>()
            val userList = dbFirestoreProfile.collection("User").whereEqualTo("wokerFlag", true).get().await().documents


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
                        val workerDB=WorkerDB(worker.id,worker.name,worker.phone,worker.email,worker.rating,worker.geoPoint,worker.skills,worker.description,worker.wokerFlag,worker.img,worker.distance)
                        workerDao.addWorker(workerDB)
                        Log.d("DBWorker",workerDB.toString())
                        workerlist.add(worker)
                    }
                }
            }
            val listRemoveWorker=localListWorker.map { it.id }.minus(workerlist.map { it.id }.toSet())
            listRemoveWorker.forEach {
                workerDao.deleteListWorker(it)
            }
            return workerlist
        } else {
            return arrayListOf<Worker>()
        }
    }


    private fun getCircleGeo(
        userGeo: GeoPosition,
        workerGeo: GeoPosition,
        maxDistance: Double
    ): Pair<String, Boolean> {
        val distance = SphericalUtil.computeDistanceBetween(
            LatLng(workerGeo.latitude, workerGeo.longitude),
            LatLng(userGeo.latitude, userGeo.longitude)
        )
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

    suspend fun deleteWorker() {
        workerDao.deleteAll()
    }
}